package infrastructure.realtime.users.subscribeduser;

public record SubscribedUserAggregateStatsChangedEventPayload(
        String userId,
        Long addCount,
        Long editCount
) { }