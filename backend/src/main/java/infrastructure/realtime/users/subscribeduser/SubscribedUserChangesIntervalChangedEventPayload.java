package infrastructure.realtime.users.subscribeduser;

public record SubscribedUserChangesIntervalChangedEventPayload(
        String startTimestamp,
        Long durationInMinutes,
        Long changesCount,
        String userId
) { }