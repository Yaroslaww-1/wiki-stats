package infrastructure.postgres.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserEntity(
        @Id String id,
        String name,
        Boolean isBot,
        Long addsCount,
        Long editsCount
) { }
