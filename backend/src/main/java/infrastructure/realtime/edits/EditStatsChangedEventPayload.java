package infrastructure.realtime.edits;

import java.time.LocalDateTime;

public record EditStatsChangedEventPayload(
        String startTimestamp,
        Long durationInMinutes,
        Long addCount,
        Long editCount,
        String userId
) { }