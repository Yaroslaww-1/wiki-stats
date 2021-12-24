package infrastructure.realtime.wikis;

public record WikiCreatedEventPayload(
        String id,
        String name
) { }