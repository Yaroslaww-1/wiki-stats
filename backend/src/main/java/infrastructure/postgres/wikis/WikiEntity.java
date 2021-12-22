package infrastructure.postgres.wikis;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("wikis")
public record WikiEntity(
        @Id String id,
        String name
) { }