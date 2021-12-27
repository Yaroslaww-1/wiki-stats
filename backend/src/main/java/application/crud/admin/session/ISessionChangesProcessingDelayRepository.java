package application.crud.admin.session;

import java.time.Duration;

public interface ISessionChangesProcessingDelayRepository {
    void setChangesProcessingDelay(Duration processingDelay);
    Duration getProcessingDelay();
}
