package infrastructure.realtime.edits;

import application.edits.IEditEventsRealtimeNotifier;
import domain.edit.Edit;
import domain.user.UserEditStats;
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

    @Override
    public Mono<Void> notifySubscribedUserEditCreated(Edit edit) {
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

        realtimeNotifier.sendEvent(new Event("SubscribedUserEditCreated", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyEditStatsChanged(UserEditStats userEditStats) {
        var eventPayload = new EditStatsChangedEventPayload(
                userEditStats.getStartTimestamp().toString(),
                userEditStats.getDurationInMinutes(),
                userEditStats.getAddCount(),
                userEditStats.getEditCount(),
                userEditStats.getUserId()
        );

        realtimeNotifier.sendEvent(new Event("EditStatsChanged", eventPayload));

        return Mono.empty();
    }
}
