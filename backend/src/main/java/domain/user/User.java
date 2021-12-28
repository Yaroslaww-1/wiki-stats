package domain.user;

import java.util.UUID;

public class User {
    private String id;
    private String name;
    private Boolean isBot;
    private Long addsCount;
    private Long editsCount;

    public User(
            String id,
            String name,
            Boolean isBot,
            Long addsCount,
            Long editsCount
    ) {
        this.id = id;
        this.name = name;
        this.isBot = isBot;
        this.addsCount = addsCount;
        this.editsCount = editsCount;
    }

    public User(String name, Boolean isBot) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.isBot = isBot;
        this.addsCount = 0L;
        this.editsCount = 0L;
    }

    public void incrementAdds() {
        this.addsCount++;
    }
    public void incrementEdits() {
        this.editsCount++;
    }

    public Long getChangesCount() { return this.addsCount + this.editsCount; }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public Boolean getIsBot() { return this.isBot; }
    public Long getAddsCount() { return this.addsCount; }
    public Long getEditsCount() { return this.editsCount; }
}
