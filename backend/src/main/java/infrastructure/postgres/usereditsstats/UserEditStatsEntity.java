package infrastructure.postgres.usereditsstats;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_edit_stats")
public record UserEditStatsEntity(
        @Id String id,
        Integer day,
        Integer year,
        Long addCount,
        Long editCount,
        String userId
) { }
