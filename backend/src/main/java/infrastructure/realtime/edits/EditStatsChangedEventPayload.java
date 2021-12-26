package infrastructure.realtime.edits;

public record EditStatsChangedEventPayload(
        Integer day,
        Integer year,
        Long addCount,
        Long editCount,
        String userId
) { }