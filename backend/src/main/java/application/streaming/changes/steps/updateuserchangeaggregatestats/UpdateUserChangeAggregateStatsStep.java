package application.streaming.changes.steps.updateuserchangeaggregatestats;

import application.crud.admin.session.ISessionRepository;
import application.crud.users.IUserChangeAggregateStatsRepository;
import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.crud.users.topusers.ITopUsersEventsRealtimeNotifier;
import application.crud.users.topusers.ITopUsersRepository;
import application.streaming.contracts.IStep;
import domain.user.UserChangeAggregateStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;


@Component
public class UpdateUserChangeAggregateStatsStep implements IStep<UpdateUserChangeAggregateStatsStepInput, UserChangeAggregateStats> {
    private final IUserChangeAggregateStatsRepository userChangeAggregateStatsRepository;
    private final ITopUsersRepository topUsersRepository;
    private final ITopUsersEventsRealtimeNotifier topUsersEventsRealtimeNotifier;
    private final ISessionRepository sessionRepository;
    private final ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier;

    public UpdateUserChangeAggregateStatsStep(
            IUserChangeAggregateStatsRepository userChangeAggregateStatsRepository,
            ITopUsersRepository topUsersRepository,
            ITopUsersEventsRealtimeNotifier topUsersEventsRealtimeNotifier, ISessionRepository sessionRepository,
            ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier
    ) {
        this.userChangeAggregateStatsRepository = userChangeAggregateStatsRepository;
        this.topUsersRepository = topUsersRepository;
        this.topUsersEventsRealtimeNotifier = topUsersEventsRealtimeNotifier;
        this.sessionRepository = sessionRepository;
        this.subscribedUserEventsRealtimeNotifier = subscribedUserEventsRealtimeNotifier;
    }

    @Override
    public Mono<UserChangeAggregateStats> execute(UpdateUserChangeAggregateStatsStepInput input) {
        return userChangeAggregateStatsRepository
                .getOne(query(where("user_id").is(input.change().getEditor().getId())))
                .map(stats -> {
                    var change = input.change();

                    if (change.isEdit()) {
                        stats.incrementEdits();
                    }

                    if (change.isAdd()) {
                        stats.incrementAdds();
                    }

                    return stats;
                })
                .delayUntil(userChangeAggregateStatsRepository::update)
                .delayUntil(stats -> topUsersRepository
                        .insertAndReturnOrderedByInterval(
                                input.change().getEditor().getName(),
                                stats.getEditCount(),
                                sessionRepository.getTopUsersInterval()
                        )
                        .delayUntil(topUsersEventsRealtimeNotifier::notifyTopUsersChanged)
                )
                .delayUntil(this::notifySubscribedUserChangeAggregateStatsChanged);
    }

    private Mono<Void> notifySubscribedUserChangeAggregateStatsChanged(UserChangeAggregateStats userChangeAggregateStats) {
        return Mono.just(userChangeAggregateStats)
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getUserId()))
                .delayUntil(subscribedUserEventsRealtimeNotifier::notifyAggregateStatsChanged)
                .then(Mono.empty());
    }
}