package infrastructure.postgres.usereditsstats;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_edit_stats")
public record UserEditStatsEntity(
        @Id String id,
        LocalDateTime startTimestamp,
        Long durationInMinutes,
        Long addCount,
        Long editCount,
        String userId
) { }
