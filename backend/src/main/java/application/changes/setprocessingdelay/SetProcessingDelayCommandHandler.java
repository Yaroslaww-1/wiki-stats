package application.changes.setprocessingdelay;

import application.contracts.ICommandHandler;
import application.changes.IChangesProcessingDelayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SetProcessingDelayCommandHandler implements ICommandHandler<SetProcessingDelayCommand, Void> {
    private final IChangesProcessingDelayManager changesProcessingDelayManager;

    @Autowired
    public SetProcessingDelayCommandHandler(
            IChangesProcessingDelayManager changesProcessingDelayManager
    ) {
        this.changesProcessingDelayManager = changesProcessingDelayManager;
    }

    @Override
    public Mono<Void> execute(SetProcessingDelayCommand query) {
        changesProcessingDelayManager.setDelay(Duration.ofMillis(query.delay()));
        return Mono.empty();
    }
}