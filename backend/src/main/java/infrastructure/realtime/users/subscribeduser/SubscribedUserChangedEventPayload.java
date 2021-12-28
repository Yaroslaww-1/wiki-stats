package infrastructure.realtime.users.subscribeduser;

public record SubscribedUserChangedEventPayload(
        String userId,
        Long addCount,
        Long editCount
) { }