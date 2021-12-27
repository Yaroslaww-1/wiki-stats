package application.streaming.changes.steps.updateuserchangestats;

import application.crud.admin.session.ISessionRepository;
import application.crud.users.IUserChangeStatsRepository;
import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.streaming.changes.steps.createchange.IChangeEventsRealtimeNotifier;
import application.streaming.contracts.IStep;
import domain.user.UserChangeStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class UpdateUserChangeStatsStep implements IStep<UpdateUserChangeStatsStepInput, UserChangeStats> {
    private final IUserChangeStatsRepository userChangeStatsRepository;
    private final ISessionRepository sessionRepository;
    private final ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier;

    public UpdateUserChangeStatsStep(
            IUserChangeStatsRepository userChangeStatsRepository,
            ISessionRepository sessionRepository,
            ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier
    ) {
        this.userChangeStatsRepository = userChangeStatsRepository;
        this.sessionRepository = sessionRepository;
        this.subscribedUserEventsRealtimeNotifier = subscribedUserEventsRealtimeNotifier;
    }

    @Override
    public Mono<UserChangeStats> execute(UpdateUserChangeStatsStepInput input) {
        var windowInMinutes = 1L;

        return userChangeStatsRepository
                .getOne(query(where("user_id").is(input.change().getEditor().getId())))
                .filter(stats ->
                        stats.getStartTimestamp().plusMinutes(windowInMinutes).isAfter(LocalDateTime.now())
                )
                .switchIfEmpty(userChangeStatsRepository.add(
                        new UserChangeStats(input.change().getEditor().getId(), windowInMinutes)
                ))
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
                .delayUntil(userChangeStatsRepository::update)
                .delayUntil(this::notifySubscribedUserChangeStatsChanged);
    }

    private Mono<UserChangeStats> notifySubscribedUserChangeStatsChanged(UserChangeStats userChangeStats) {
        return Mono.just(userChangeStats)
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getUserId()))
                .delayUntil(subscribedUserEventsRealtimeNotifier::notifyStatsChanged);
    }
}