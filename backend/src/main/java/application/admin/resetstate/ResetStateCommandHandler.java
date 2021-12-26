package application.admin.resetstate;

import application.contracts.ICommandHandler;
import application.changes.IChangesProcessingDelayManager;
import application.users.IChangesSubscriptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ResetStateCommandHandler implements ICommandHandler<ResetStateCommand, Void> {
    private final IChangesProcessingDelayManager changesProcessingDelayManager;
    private final IChangesSubscriptionManager changesSubscriptionManager;

    @Autowired
    public ResetStateCommandHandler(
            IChangesProcessingDelayManager changesProcessingDelayManager,
            IChangesSubscriptionManager changesSubscriptionManager
    ) {
        this.changesProcessingDelayManager = changesProcessingDelayManager;
        this.changesSubscriptionManager = changesSubscriptionManager;
    }

    @Override
    public Mono<Void> execute(ResetStateCommand command) {
        changesProcessingDelayManager.setDelay(Duration.ZERO);
        changesSubscriptionManager.unsubscribeAll();
        return Mono.empty();
    }
}