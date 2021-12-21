package domain.user;

import java.util.UUID;

public class User {
    private String id;
    private String name;
    private Boolean isBot;

    public User(String id, String name, Boolean isBot) {
        this.id = id;
        this.name = name;
        this.isBot = isBot;
    }

    public User(String name, Boolean isBot) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.isBot = isBot;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public Boolean getIsBot() { return this.isBot; }
}
