package infrastructure.realtime.changes;

import java.time.LocalDateTime;

public record ChangeCreatedEventPayload(
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        EditorOfChangeCreatedEventPayload editor,
        WikiOfChangeCreatedEventPayload wiki
) { }

record EditorOfChangeCreatedEventPayload(
        String id,
        String name
) { }

record WikiOfChangeCreatedEventPayload(
        String id,
        String name
) { }