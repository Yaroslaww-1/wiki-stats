package application.edits.setprocessingdelay;

import application.contracts.ICommandHandler;
import application.edits.WikimediaServerSendEventsProcessingDelayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SetProcessingDelayCommandHandler implements ICommandHandler<SetProcessingDelayCommand, Void> {
    private final WikimediaServerSendEventsProcessingDelayManager wikimediaServerSendEventsProcessingDelayManager;

    @Autowired
    public SetProcessingDelayCommandHandler(WikimediaServerSendEventsProcessingDelayManager wikimediaServerSendEventsProcessingDelayManager) {
        this.wikimediaServerSendEventsProcessingDelayManager = wikimediaServerSendEventsProcessingDelayManager;
    }

    @Override
    public Mono<Void> execute(SetProcessingDelayCommand query) {
        wikimediaServerSendEventsProcessingDelayManager.setDelay(Duration.ofMillis(query.delay()));
        return Mono.empty();
    }
}