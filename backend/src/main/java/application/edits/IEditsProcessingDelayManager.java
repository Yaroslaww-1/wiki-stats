package application.edits;

import java.time.Duration;

public interface IEditsProcessingDelayManager {
    void setDelay(Duration delay);
    Duration getDelay();
}
