package infrastructure.session;

import application.admin.session.ISessionChangesProcessingDelayRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SessionChangesProcessingDelayRepository implements ISessionChangesProcessingDelayRepository {
    private final AtomicLong delay = new AtomicLong(0);

    public void setChangesProcessingDelay(Duration delay) {
        this.delay.set(delay.toMillis());
    }

    public Duration getProcessingDelay() {
        return Duration.ofMillis(delay.get());
    }
}
