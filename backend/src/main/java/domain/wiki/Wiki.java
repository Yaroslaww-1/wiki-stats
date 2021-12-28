package domain.wiki;

import java.util.UUID;

public class Wiki {
    private String id;
    private String name;
    private Long editsCount;

    public Wiki(String id, String name, Long editsCount) {
        this.id = id;
        this.name = name;
        this.editsCount = editsCount;
    }

    public Wiki(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.editsCount = 0L;
    }

    public void incrementEdits() {
        this.editsCount++;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public Long getEditsCount() { return this.editsCount; }
}
