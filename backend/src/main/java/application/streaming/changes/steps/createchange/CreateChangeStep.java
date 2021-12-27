package application.streaming.changes.steps.createchange;

import application.crud.admin.session.ISessionRepository;
import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.streaming.contracts.IStep;
import domain.change.Change;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CreateChangeStep implements IStep<CreateChangeStepInput, Change> {
    private final IChangeRepository changeRepository;
    private final IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier;
    private final ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier;
    private final ISessionRepository sessionRepository;

    public CreateChangeStep(
            IChangeRepository changeRepository,
            IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier,
            ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier,
            ISessionRepository sessionRepository
    ) {
        this.changeRepository = changeRepository;
        this.changeEventsRealtimeNotifier = changeEventsRealtimeNotifier;
        this.subscribedUserEventsRealtimeNotifier = subscribedUserEventsRealtimeNotifier;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Mono<Change> execute(CreateChangeStepInput input) {
        return Mono.just(
                    new Change(
                        input.id(),
                        input.timestamp(),
                        input.title(),
                        input.comment(),
                        input.type(),
                        input.user(),
                        input.wiki()
                    )
                )
                .delayUntil(changeRepository::add)
                .delayUntil(changeEventsRealtimeNotifier::notifyChangeCreated)
                .delayUntil(this::notifySubscribedUserChangeCreated);
    }

    private Mono<Change> notifySubscribedUserChangeCreated(Change change) {
        return Mono.just(change)
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getEditor().getId()))
                .delayUntil(subscribedUserEventsRealtimeNotifier::notifyChangeCreated);
    }
}
