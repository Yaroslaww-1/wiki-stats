package infrastructure.realtime.changes;

public record UserChangeAggregateStatsChangedEventPayload(
        String userId,
        Long addCount,
        Long editCount
) { }