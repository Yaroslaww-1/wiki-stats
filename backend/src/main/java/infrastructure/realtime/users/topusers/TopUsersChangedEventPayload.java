package infrastructure.realtime.users.topusers;

import java.util.List;

record TopUsersEventPayload(
        String userName,
        Long changesCount
) {}

public record TopUsersChangedEventPayload(
        List<TopUsersEventPayload> users
) { }