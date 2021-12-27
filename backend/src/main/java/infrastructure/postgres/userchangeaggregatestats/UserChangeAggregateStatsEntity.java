package infrastructure.postgres.userchangeaggregatestats;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_change_aggregate_stats")
public record UserChangeAggregateStatsEntity(
        @Id String id,
        String userId,
        Long addCount,
        Long editCount
) { }
