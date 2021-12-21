package domain.wiki;

import java.util.UUID;

public class Wiki {
    private String id;
    private String name;

    public Wiki(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Wiki(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
}
