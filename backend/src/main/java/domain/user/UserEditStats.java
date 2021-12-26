package domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserEditStats {
    private String id;
    private Integer day;
    private Integer year;
    private Long addCount;
    private Long editCount;
    private String userId;

    public UserEditStats(
            String id,
            Integer day,
            Integer year,
            Long addCount,
            Long editCount,
            String userId
    ) {
        this.id = id;
        this.day = day;
        this.year = year;
        this.addCount = addCount;
        this.editCount = editCount;
        this.userId = userId;
    }

    public UserEditStats(
            String userId
    ) {
        this.id = UUID.randomUUID().toString();
        this.day = LocalDateTime.now().getDayOfYear();
        this.year = LocalDateTime.now().getYear();
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

    public String getId() { return this.id; }
    public Integer getDay() { return this.day; }
    public Integer getYear() { return this.year; }
    public Long getAddCount() { return this.addCount; }
    public Long getEditCount() { return this.editCount; }
    public String getUserId() { return this.userId; }
}
