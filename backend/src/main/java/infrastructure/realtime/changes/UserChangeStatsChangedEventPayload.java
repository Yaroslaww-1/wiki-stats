package infrastructure.realtime.changes;

public record UserChangeStatsChangedEventPayload(
        String startTimestamp,
        Long durationInMinutes,
        Long changesCount,
        String userId
) { }