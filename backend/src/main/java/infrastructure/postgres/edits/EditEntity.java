package infrastructure.postgres.edits;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("edits")
public record EditEntity(
        @Id String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String editorId,
        String wikiId
) { }
