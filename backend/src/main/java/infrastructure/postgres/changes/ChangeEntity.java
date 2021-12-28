package infrastructure.postgres.changes;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("changes")
public record ChangeEntity(
        @Id String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String type,
        String userId,
        String wikiId
) { }
