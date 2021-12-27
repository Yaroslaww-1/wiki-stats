package infrastructure.postgres.userchangestats;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_change_stats")
public record UserChangeStatsEntity(
        @Id String id,
        LocalDateTime startTimestamp,
        Long durationInMinutes,
        Long addCount,
        Long editCount,
        String userId
) { }
