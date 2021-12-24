package infrastructure.realtime.edits;

import application.edits.IEditEventsRealtimeNotifier;
import domain.edit.Edit;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EditEventsRealtimeNotifier implements IEditEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public EditEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyEditCreated(Edit edit) {
        var eventPayload = new EditCreatedEventPayload(
                edit.getId(),
                edit.getTimestamp(),
                edit.getTitle(),
                edit.getComment(),
                new EditorOfEditCreatedEventPayload(
                        edit.getEditor().getId(),
                        edit.getEditor().getName()
                ),
                new WikiOfEditCreatedEventPayload(
                        edit.getWiki().getId(),
                        edit.getWiki().getName()
                )
        );

        realtimeNotifier.sendEvent(new Event("EditCreated", eventPayload));

        return Mono.empty();
    }
}
