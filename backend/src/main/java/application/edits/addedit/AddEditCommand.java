package application.edits.addedit;

import java.time.LocalDateTime;

public record AddEditCommand(
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String editor,
        Boolean isBot,
        String wiki,
        String type
) { }
