package infrastructure.realtime.users;

import java.util.List;

record TopUsersEventPayload(
        String userName,
        Long changesCount
) {}

public record TopUsersChangedEventPayload(
        List<TopUsersEventPayload> users
) { }