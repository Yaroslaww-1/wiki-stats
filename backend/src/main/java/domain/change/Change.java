package domain.change;

import domain.wiki.Wiki;
import domain.user.User;

import java.time.LocalDateTime;

public class Change {
    private String id;
    private LocalDateTime timestamp;
    private String title;
    private String comment;
    private String type;

    private User user;
    private Wiki wiki;

    public Change(
            String id,
            LocalDateTime timestamp,
            String title,
            String comment,
            String type,
            User user,
            Wiki wiki
    ) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.comment = comment;
        this.type = type;
        this.user = user;
        this.wiki = wiki;
    }

    public Boolean isEdit() {
        return this.type.equals("edit");
    }

    public Boolean isAdd() {
        return this.type.equals("new");
    }

    public String getId() { return this.id; }
    public LocalDateTime getTimestamp() { return this.timestamp; }
    public String getTitle() { return this.title; }
    public String getComment() { return this.comment; }
    public String getType() { return this.type; }

    public User getUser() { return this.user; }
    public Wiki getWiki() { return this.wiki; }
}
