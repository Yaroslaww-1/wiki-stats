package infrastructure.realtime.changes;

import application.changes.IChangeEventsRealtimeNotifier;
import domain.change.Change;
import domain.user.UserChangeStats;
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

    @Override
    public Mono<Void> notifySubscribedUserChangeCreated(Change change) {
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

        realtimeNotifier.sendEvent(new Event("SubscribedUserChangeCreated", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyChangeStatsChanged(UserChangeStats userChangeStats) {
        var eventPayload = new ChangeStatsChangedEventPayload(
                userChangeStats.getStartTimestamp().toString(),
                userChangeStats.getDurationInMinutes(),
                userChangeStats.getChangesCount(),
                userChangeStats.getUserId()
        );

        realtimeNotifier.sendEvent(new Event("ChangeStatsChanged", eventPayload));

        return Mono.empty();
    }
}
