package domain.edit;

import domain.wiki.Wiki;
import domain.user.User;

import java.time.LocalDateTime;

public class Edit {
    private String id;
    private LocalDateTime timestamp;
    private String title;
    private String comment;

    private User editor;
    private Wiki wiki;

    public Edit(
            String id,
            LocalDateTime timestamp,
            String title,
            String comment,
            User editor,
            Wiki wiki
    ) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.comment = comment;
        this.editor = editor;
        this.wiki = wiki;
    }

    public String getId() { return this.id; }
    public LocalDateTime getTimestamp() { return this.timestamp; }
    public String getTitle() { return this.title; }
    public String getComment() { return this.comment; }

    public User getEditor() { return this.editor; }
    public Wiki getWiki() { return this.wiki; }
}