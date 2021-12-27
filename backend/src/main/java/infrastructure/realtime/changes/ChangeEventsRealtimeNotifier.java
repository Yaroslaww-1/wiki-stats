package infrastructure.realtime.changes;

import application.streaming.changes.steps.createchange.IChangeEventsRealtimeNotifier;
import domain.change.Change;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ChangeEventsRealtimeNotifier implements IChangeEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public ChangeEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyChangeCreated(Change change) {
        var eventPayload = new ChangeCreatedEventPayload(
                change.getId(),
                change.getTimestamp(),
                change.getTitle(),
                change.getComment(),
                new EditorOfChangeCreatedEventPayload(
                        change.getEditor().getId(),
                        change.getEditor().getName()
                ),
                new WikiOfChangeCreatedEventPayload(
                        change.getWiki().getId(),
                        change.getWiki().getName()
                )
        );

        realtimeNotifier.sendEvent(new Event("ChangeCreated", eventPayload));

        return Mono.empty();
    }
}
