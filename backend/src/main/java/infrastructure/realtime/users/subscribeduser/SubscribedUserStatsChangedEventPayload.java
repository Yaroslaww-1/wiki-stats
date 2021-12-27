package infrastructure.realtime.users.subscribeduser;

public record SubscribedUserStatsChangedEventPayload(
        String startTimestamp,
        Long durationInMinutes,
        Long changesCount,
        String userId
) { }