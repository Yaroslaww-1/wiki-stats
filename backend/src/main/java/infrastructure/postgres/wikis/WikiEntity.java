package infrastructure.postgres.wikis;

import org.springframework.data.annotation.Id;

public record WikiEntity(
        @Id String id,
        String name
) { }