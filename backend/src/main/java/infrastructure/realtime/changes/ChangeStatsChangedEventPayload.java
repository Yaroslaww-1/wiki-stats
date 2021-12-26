package infrastructure.realtime.changes;

import java.time.LocalDateTime;

public record ChangeStatsChangedEventPayload(
        String startTimestamp,
        Long durationInMinutes,
        Long changesCount,
        String userId
) { }