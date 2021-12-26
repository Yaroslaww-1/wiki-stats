package application.admin.resetstate;

import application.contracts.ICommandHandler;
import application.edits.IEditsProcessingDelayManager;
import application.users.IEditsSubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ResetStateCommandHandler implements ICommandHandler<ResetStateCommand, Void> {
    private final IEditsProcessingDelayManager editsProcessingDelayManager;
    private final IEditsSubscriptionManager editsSubscriptionManager;

    @Autowired
    public ResetStateCommandHandler(
            IEditsProcessingDelayManager editsProcessingDelayManager,
            IEditsSubscriptionManager editsSubscriptionManager
    ) {
        this.editsProcessingDelayManager = editsProcessingDelayManager;
        this.editsSubscriptionManager = editsSubscriptionManager;
    }

    @Override
    public Mono<Void> execute(ResetStateCommand command) {
        editsProcessingDelayManager.setDelay(Duration.ZERO);
        editsSubscriptionManager.unsubscribeAll();
        return Mono.empty();
    }
}