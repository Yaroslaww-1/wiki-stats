package application.streaming.changes.flows.addchange;

import java.time.LocalDateTime;

public record AddChangeFlowInput(
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String editor,
        Boolean isBot,
        String wiki,
        String type
) { }