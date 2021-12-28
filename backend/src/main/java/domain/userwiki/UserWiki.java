package domain.userwiki;

import java.util.UUID;

public class UserWiki {
    private String id;
    private Long changesCount;
    private String userId;
    private String wikiId;
    private String wikiName;

    public UserWiki(
            String id,
            Long changesCount,
            String userId,
            String wikiId,
            String wikiName
    ) {
        this.id = id;
        this.changesCount = changesCount;
        this.userId = userId;
        this.wikiId = wikiId;
        this.wikiName = wikiName;
    }

    public UserWiki(
            Long changesCount,
            String userId,
            String wikiId,
            String wikiName
    ) {
        this.id = UUID.randomUUID().toString();
        this.changesCount = changesCount;
        this.userId = userId;
        this.wikiId = wikiId;
        this.wikiName = wikiName;
    }

    public void incrementChanges() {
        this.changesCount++;
    }


    public String getId() { return this.id; }
    public Long getChangesCount() { return this.changesCount; }
    public String getUserId() { return this.userId; }
    public String getWikiId() { return this.wikiId; }
    public String getWikiName() { return this.wikiName; }
}