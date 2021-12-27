package application.streaming.changes.steps.updateuserwikichangestats;

import application.crud.admin.session.ISessionRepository;
import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.streaming.changes.IUserWikiChangeStatsRepository;
import application.streaming.contracts.IStep;
import domain.change.Change;
import domain.user.UserWikiChangeStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class UpdateUserWikiChangeStatsStep implements IStep<UpdateUserWikiChangeStatsStepInput, UserWikiChangeStats> {
    private final ISessionRepository sessionRepository;
    private final IUserWikiChangeStatsRepository userWikiChangeStatsRepository;
    private final ITopUserWikiRepository topUserWikiRepository;
    private final ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier;

    public UpdateUserWikiChangeStatsStep(
            ISessionRepository sessionRepository,
            IUserWikiChangeStatsRepository userWikiChangeStatsRepository,
            ITopUserWikiRepository topUserWikiRepository,
            ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier
    ) {
        this.sessionRepository = sessionRepository;
        this.userWikiChangeStatsRepository = userWikiChangeStatsRepository;
        this.topUserWikiRepository = topUserWikiRepository;
        this.subscribedUserEventsRealtimeNotifier = subscribedUserEventsRealtimeNotifier;
    }

    @Override
    public Mono<UserWikiChangeStats> execute(UpdateUserWikiChangeStatsStepInput input) {
        return userWikiChangeStatsRepository
                .getOne(
                        query(
                                where("user_id").is(input.change().getEditor().getId()).and(
                                where("wiki_id").is(input.change().getWiki().getId()))
                        )
                )
                .switchIfEmpty(userWikiChangeStatsRepository.add(
                        new UserWikiChangeStats(
                                0L,
                                input.change().getEditor().getId(),
                                input.change().getWiki().getId()
                        )
                ))
                .map(stats -> {
                    var change = input.change();

                    if (change.isEdit() || change.isAdd()) {
                        stats.incrementChanges();
                    }

                    return stats;
                })
                .delayUntil(userWikiChangeStatsRepository::update)
                .delayUntil(stats -> this.notifySubscribedUserWikiChangeStatsChanged(stats, input.change()));
    }

    private Mono<Void> notifySubscribedUserWikiChangeStatsChanged(UserWikiChangeStats userWikiChangeStats, Change change) {
        return Mono.just(userWikiChangeStats)
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getUserId()))
                .flatMap(stats -> topUserWikiRepository.insertAndReturnOrdered(
                        change.getEditor().getId(),
                        new TopUserWiki(stats.getChangesCount(), change.getWiki().getName())
                ))
                .delayUntil(statsList -> subscribedUserEventsRealtimeNotifier.notifyTopWikisChanged(
                        change.getEditor().getId(),
                        statsList
                ))
                .then(Mono.empty());
    }
}