package infrastructure.realtime.users.subscribeduser;

import java.time.LocalDateTime;

public record SubscribedUserChangeCreatedEventPayload(
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String userName,
        String wikiName
) { }
