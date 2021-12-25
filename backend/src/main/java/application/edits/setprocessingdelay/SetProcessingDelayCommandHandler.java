package application.edits.setprocessingdelay;

import application.contracts.ICommandHandler;
import application.edits.IEditsProcessingDelayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SetProcessingDelayCommandHandler implements ICommandHandler<SetProcessingDelayCommand, Void> {
    private final IEditsProcessingDelayManager editsProcessingDelayManager;

    @Autowired
    public SetProcessingDelayCommandHandler(
            IEditsProcessingDelayManager editsProcessingDelayManager
    ) {
        this.editsProcessingDelayManager = editsProcessingDelayManager;
    }

    @Override
    public Mono<Void> execute(SetProcessingDelayCommand query) {
        editsProcessingDelayManager.setDelay(Duration.ofMillis(query.delay()));
        return Mono.empty();
    }
}