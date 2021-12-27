package domain.user;

import java.util.UUID;

public class UserWikiChangeStats {
    private String id;
    private Long changesCount;
    private String userId;
    private String wikiId;

    public UserWikiChangeStats(
            String id,
            Long changesCount,
            String userId,
            String wikiId
    ) {
        this.id = id;
        this.changesCount = changesCount;
        this.userId = userId;
        this.wikiId = wikiId;
    }

    public UserWikiChangeStats(
            Long changesCount,
            String userId,
            String wikiId
    ) {
        this.id = UUID.randomUUID().toString();
        this.changesCount = changesCount;
        this.userId = userId;
        this.wikiId = wikiId;
    }

    public void incrementChanges() {
        this.changesCount++;
    }


    public String getId() { return this.id; }
    public Long getChangesCount() { return this.changesCount; }
    public String getUserId() { return this.userId; }
    public String getWikiId() { return this.wikiId; }
}