package domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserChangeStats {
    private String id;
    private LocalDateTime startTimestamp;
    private Long durationInMinutes;
    private Long addCount;
    private Long editCount;
    private String userId;

    public UserChangeStats(
            String id,
            LocalDateTime startTimestamp,
            Long durationInMinutes,
            Long addCount,
            Long editCount,
            String userId
    ) {
        this.id = id;
        this.startTimestamp = startTimestamp;
        this.durationInMinutes = durationInMinutes;
        this.addCount = addCount;
        this.editCount = editCount;
        this.userId = userId;
    }

    public UserChangeStats(
            String userId,
            Long durationInMinutes
    ) {
        this.id = UUID.randomUUID().toString();
        this.startTimestamp = LocalDateTime.now();
        this.durationInMinutes = durationInMinutes;
        this.addCount = 0L;
        this.editCount = 0L;
        this.userId = userId;
    }

    public void incrementAdds() {
        this.addCount++;
    }

    public void incrementEdits() {
        this.editCount++;
    }

    public Long getChangesCount() {
        return this.addCount + this.editCount;
    }

    public String getId() { return this.id; }
    public LocalDateTime getStartTimestamp() { return this.startTimestamp; }
    public Long getDurationInMinutes() { return this.durationInMinutes; }
    public Long getAddCount() { return this.addCount; }
    public Long getEditCount() { return this.editCount; }
    public String getUserId() { return this.userId; }
}
