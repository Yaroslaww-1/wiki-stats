package domain.user;

import java.util.UUID;

public class UserChangeAggregateStats {
    private String id;
    private String userId;
    private Long addCount;
    private Long editCount;

    public UserChangeAggregateStats(
            String id,
            String userId,
            Long addCount,
            Long editCount
    ) {
        this.id = id;
        this.userId = userId;
        this.addCount = addCount;
        this.editCount = editCount;
    }

    public UserChangeAggregateStats(String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.addCount = 0L;
        this.editCount = 0L;
    }

    public void incrementAdds() {
        this.addCount++;
    }

    public void incrementEdits() {
        this.editCount++;
    }

    public String getId() { return this.id; }
    public String getUserId() { return this.userId; }
    public Long getAddCount() { return this.addCount; }
    public Long getEditCount() { return this.editCount; }
}