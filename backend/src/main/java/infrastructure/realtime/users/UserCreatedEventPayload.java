package infrastructure.realtime.users;

public record UserCreatedEventPayload(
        String id,
        String name,
        Boolean isBot
) { }