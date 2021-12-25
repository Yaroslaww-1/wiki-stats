package infrastructure;

import application.edits.IEditsProcessingDelayManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EditsProcessingDelayManager implements IEditsProcessingDelayManager {
    private final AtomicLong delay = new AtomicLong(0);

    public void setDelay(Duration delay) {
        this.delay.set(delay.toMillis());
    }

    public Duration getDelay() {
        return Duration.ofMillis(delay.get());
    }
}
