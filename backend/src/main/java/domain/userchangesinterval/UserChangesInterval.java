package domain.userchangesinterval;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserChangesInterval {
    private String id;
    private LocalDateTime startTimestamp;
    private Long durationInMinutes;
    private Long addsCount;
    private Long editsCount;
    private String userId;

    public UserChangesInterval(
            String id,
            LocalDateTime startTimestamp,
            Long durationInMinutes,
            Long addsCount,
            Long editsCount,
            String userId
    ) {
        this.id = id;
        this.startTimestamp = startTimestamp;
        this.durationInMinutes = durationInMinutes;
        this.addsCount = addsCount;
        this.editsCount = editsCount;
        this.userId = userId;
    }

    public UserChangesInterval(
            String userId,
            Long durationInMinutes
    ) {
        this.id = UUID.randomUUID().toString();
        this.startTimestamp = LocalDateTime.now();
        this.durationInMinutes = durationInMinutes;
        this.addsCount = 0L;
        this.editsCount = 0L;
        this.userId = userId;
    }

    public void incrementAdds() {
        this.addsCount++;
    }

    public void incrementEdits() {
        this.editsCount++;
    }

    public Long getChangesCount() {
        return this.addsCount + this.editsCount;
    }

    public String getId() { return this.id; }
    public LocalDateTime getStartTimestamp() { return this.startTimestamp; }
    public Long getDurationInMinutes() { return this.durationInMinutes; }
    public Long getAddsCount() { return this.addsCount; }
    public Long getEditsCount() { return this.editsCount; }
    public String getUserId() { return this.userId; }
}
