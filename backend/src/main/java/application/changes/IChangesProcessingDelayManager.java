package application.changes;

import java.time.Duration;

public interface IChangesProcessingDelayManager {
    void setDelay(Duration delay);
    Duration getDelay();
}
