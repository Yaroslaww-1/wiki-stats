package application.streaming.changes.steps.updatestatsandtops;

import application.crud.admin.session.ISessionRepository;
import application.streaming.contracts.IStep;
import domain.change.Change;
import domain.user.ITopUserRepository;
import domain.user.IUserEventsRealtimeNotifier;
import domain.user.IUserRepository;
import domain.user.User;
import domain.wiki.ITopWikiRepository;
import domain.wiki.IWikiEventsRealtimeNotifier;
import domain.wiki.IWikiRepository;
import domain.wiki.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class UpdateStatsAndTopsStep implements IStep<UpdateStatsAndTopsStepInput, Void> {
    private final IWikiRepository wikiRepository;
    private final ITopWikiRepository topWikiRepository;
    private final IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier;

    private final IUserRepository userRepository;
    private final ITopUserRepository topUserRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;

    private final ISessionRepository sessionRepository;

    @Autowired
    public UpdateStatsAndTopsStep(
            IWikiRepository wikiRepository,
            ITopWikiRepository topWikiRepository,
            IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier,
            IUserRepository userRepository,
            ITopUserRepository topUserRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier,
            ISessionRepository sessionRepository
    ) {
        this.wikiRepository = wikiRepository;
        this.topWikiRepository = topWikiRepository;
        this.wikiEventsRealtimeNotifier = wikiEventsRealtimeNotifier;
        this.userRepository = userRepository;
        this.topUserRepository = topUserRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Mono<Void> execute(UpdateStatsAndTopsStepInput input) {
        return Mono
                .zip(
                    this.updateWikiStatsAndTops(input.wiki(), input.change()),
                    this.updateUserStatsAndTops(input.user(), input.change())
                )
                .then(Mono.empty());
    }

    private Mono<List<Wiki>> updateWikiStatsAndTops(Wiki inputWiki, Change change) {
        return Mono
                .just(inputWiki)
                .map(wiki -> {
                    if (change.isEdit()) {
                        wiki.incrementEdits();
                    }

                    return wiki;
                })
                .delayUntil(wikiRepository::update)
                .flatMap(topWikiRepository::insertAndReturnOrdered)
                .delayUntil(wikiEventsRealtimeNotifier::notifyTopWikisChanged);
    }

    private Mono<List<User>> updateUserStatsAndTops(User inputUser, Change change) {
        return Mono
                .just(inputUser)
                .map(user -> {
                    if (change.isEdit()) {
                        user.incrementEdits();
                    }

                    if (change.isAdd()) {
                        user.incrementAdds();
                    }

                    return user;
                })
                .delayUntil(userRepository::update)
                .flatMap(user -> topUserRepository.insertAndReturnOrderedByInterval(user, sessionRepository.getTopUsersInterval()))
                .delayUntil(userEventsRealtimeNotifier::notifyTopUsersChanged);
    }
}