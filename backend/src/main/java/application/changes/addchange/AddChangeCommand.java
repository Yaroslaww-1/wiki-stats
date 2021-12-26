package application.changes.addchange;

import java.time.LocalDateTime;

public record AddChangeCommand(
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String editor,
        Boolean isBot,
        String wiki,
        String type
) { }
