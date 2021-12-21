package infrastructure.postgres.edits;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record EditEntity(
        @Id String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String editorId,
        String wikiId
) { }
