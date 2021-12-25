package application.edits.setprocessingdelay;

import application.contracts.ICommandHandler;
import application.edits.IWikimediaServerSendEventsProcessingDelayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SetProcessingDelayCommandHandler implements ICommandHandler<SetProcessingDelayCommand, Void> {
    private final IWikimediaServerSendEventsProcessingDelayManager wikimediaServerSendEventsProcessingDelayManager;

    @Autowired
    public SetProcessingDelayCommandHandler(
            IWikimediaServerSendEventsProcessingDelayManager wikimediaServerSendEventsProcessingDelayManager
    ) {
        this.wikimediaServerSendEventsProcessingDelayManager = wikimediaServerSendEventsProcessingDelayManager;
    }

    @Override
    public Mono<Void> execute(SetProcessingDelayCommand query) {
        wikimediaServerSendEventsProcessingDelayManager.setDelay(Duration.ofMillis(query.delay()));
        return Mono.empty();
    }
}