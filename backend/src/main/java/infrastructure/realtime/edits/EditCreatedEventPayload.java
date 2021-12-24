package infrastructure.realtime.edits;

import java.time.LocalDateTime;

public record EditCreatedEventPayload(
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        EditorOfEditCreatedEventPayload editor,
        WikiOfEditCreatedEventPayload wiki
) { }

record EditorOfEditCreatedEventPayload(
        String id,
        String name
) { }

record WikiOfEditCreatedEventPayload(
        String id,
        String name
) { }