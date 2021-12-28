package application.streaming.changes.steps.createchange;

import application.crud.admin.session.ISessionRepository;
import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.streaming.contracts.IStep;
import domain.change.Change;
import domain.change.IChangeEventsRealtimeNotifier;
import domain.change.IChangeRepository;
import domain.user.User;
import domain.wiki.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

@Component
public class CreateChangeStep implements IStep<CreateChangeStepInput, Tuple3<User, Wiki, Change>> {
    private final IChangeRepository changeRepository;
    private final IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier;
    private final ISubscribedUserEventsRealtimeNotifier subscribedUserEventsRealtimeNotifier;
    private final ISessionRepository sessionRepository;

    @Autowired
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
    public Mono<Tuple3<User, Wiki, Change>> execute(CreateChangeStepInput input) {
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
                .flatMap(value -> Mono.zip(
                            Mono.just(input.user()),
                            Mono.just(input.wiki()),
                            Mono.just(value)
                        )
                );
    }
}
