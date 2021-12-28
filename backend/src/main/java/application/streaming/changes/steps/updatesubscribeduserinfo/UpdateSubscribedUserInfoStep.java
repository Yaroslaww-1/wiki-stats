package application.streaming.changes.steps.updatesubscribeduserinfo;

import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.streaming.contracts.IStep;
import domain.change.Change;
import domain.user.User;
import domain.userchangesinterval.IUserChangesIntervalRepository;
import domain.userchangesinterval.UserChangesInterval;
import domain.userwiki.ITopUserWikiRepository;
import domain.userwiki.IUserWikiRepository;
import domain.userwiki.UserWiki;
import domain.wiki.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;


@Component
public class UpdateSubscribedUserInfoStep implements IStep<UpdateSubscribedUserInfoStepInput, Void> {
    private final ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier;
    private final ITopUserWikiRepository topUserWikiRepository;
    private final IUserWikiRepository userWikiRepository;
    private final IUserChangesIntervalRepository userChangesIntervalRepository;

    @Autowired
    public UpdateSubscribedUserInfoStep(
            ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier,
            ITopUserWikiRepository topUserWikiRepository,
            IUserWikiRepository userWikiRepository,
            IUserChangesIntervalRepository userChangesIntervalRepository
    ) {
        this.subscribedUserEventsRealtimeNotifier = subscribedUserEventsRealtimeNotifier;
        this.topUserWikiRepository = topUserWikiRepository;
        this.userWikiRepository = userWikiRepository;
        this.userChangesIntervalRepository = userChangesIntervalRepository;
    }

    @Override
    public Mono<Void> execute(UpdateSubscribedUserInfoStepInput input) {
        return Mono
                .zip(
                        this.notifySubscribedUserChanged(input.user()),
                        this.updateAndNotifySubscribedUserTopWikisChanged(input.user(), input.wiki(), input.change()),
                        this.updateAndNotifySubscribedUserLatestChangesIntervalChanged(input.user(), input.wiki(), input.change()),
                        this.notifySubscribedUserChangeCreated(input.change())
                )
                .then(Mono.empty());
    }

    private Mono<User> notifySubscribedUserChanged(User user) {
        return Mono.just(user).delayUntil(subscribedUserEventsRealtimeNotifier::notifyUserChanged);
    }

    private Mono<Void> updateAndNotifySubscribedUserTopWikisChanged(User inputUser, Wiki inputWiki, Change change) {
        return userWikiRepository
                .getOne(
                        query(
                                where("user_id").is(inputUser.getId()).and(
                                where("wiki_id").is(inputWiki.getId()))
                        )
                )
                .switchIfEmpty(userWikiRepository.add(
                        new UserWiki(
                                0L,
                                inputUser.getId(),
                                inputWiki.getId(),
                                inputWiki.getName()
                        )
                ))
                .map(userWiki -> {
                    if (change.isEdit() || change.isAdd()) {
                        userWiki.incrementChanges();
                    }
                    return userWiki;
                })
                .delayUntil(userWikiRepository::update)
                .flatMap(stats -> topUserWikiRepository.insertAndReturnOrdered(
                        change.getUser().getId(),
                        new UserWiki(
                                stats.getChangesCount(),
                                inputUser.getId(),
                                inputWiki.getId(),
                                inputWiki.getName()
                        )
                ))
                .delayUntil(statsList -> subscribedUserEventsRealtimeNotifier.notifyTopWikisChanged(
                        inputUser.getId(),
                        statsList
                ))
                .then(Mono.empty());
    }

    //TODO: use cache here similarly to TopUsersRepository
    private Mono<Void> updateAndNotifySubscribedUserLatestChangesIntervalChanged(User inputUser, Wiki inputWiki, Change change) {
        var windowInMinutes = 1L;

        return userChangesIntervalRepository
                .getOne(query(where("user_id").is(inputUser.getId())))
                .filter(stats ->
                        stats.getStartTimestamp().plusMinutes(windowInMinutes).isAfter(LocalDateTime.now())
                )
                .switchIfEmpty(userChangesIntervalRepository.add(
                        new UserChangesInterval(inputUser.getId(), windowInMinutes)
                ))
                .map(userChangesInterval -> {
                    if (change.isEdit()) {
                        userChangesInterval.incrementEdits();
                    }

                    if (change.isAdd()) {
                        userChangesInterval.incrementAdds();
                    }

                    return userChangesInterval;
                })
                .delayUntil(userChangesIntervalRepository::update)
                .delayUntil(subscribedUserEventsRealtimeNotifier::notifyLatestChangesIntervalChanged)
                .then(Mono.empty());
    }

    private Mono<Change> notifySubscribedUserChangeCreated(Change change) {
        return Mono.just(change).delayUntil(subscribedUserEventsRealtimeNotifier::notifyChangeCreated);
    }
}