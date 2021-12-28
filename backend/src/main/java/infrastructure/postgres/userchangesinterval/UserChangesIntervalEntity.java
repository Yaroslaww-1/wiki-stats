package infrastructure.postgres.userchangesinterval;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_changes_intervals")
public record UserChangesIntervalEntity(
        @Id String id,
        LocalDateTime startTimestamp,
        Long durationInMinutes,
        Long addsCount,
        Long editsCount,
        String userId
) { }
