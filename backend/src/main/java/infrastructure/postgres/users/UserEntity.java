package infrastructure.postgres.users;

import org.springframework.data.annotation.Id;

public record UserEntity(
        @Id String id,
        String name,
        Boolean isBot
) { }
