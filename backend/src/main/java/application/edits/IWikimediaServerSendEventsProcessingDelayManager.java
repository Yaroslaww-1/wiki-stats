package application.edits;

import java.time.Duration;

public interface IWikimediaServerSendEventsProcessingDelayManager {
    void setDelay(Duration delay);
    Duration getDelay();
}
