package infrastructure;

import application.edits.IWikimediaServerSendEventsProcessingDelayManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class WikimediaServerSendEventsProcessingDelayManager implements IWikimediaServerSendEventsProcessingDelayManager {
    private final AtomicLong delay = new AtomicLong(0);

    public void setDelay(Duration delay) {
        this.delay.set(delay.toMillis());
    }

    public Duration getDelay() {
        return Duration.ofMillis(delay.get());
    }
}
