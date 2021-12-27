package infrastructure.realtime.changes;

import application.changes.IChangeEventsRealtimeNotifier;
import application.changes.UserWikiChangeStatsOrdered;
import domain.change.Change;
import domain.user.UserChangeAggregateStats;
import domain.user.UserChangeStats;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<Void> notifyUserChangeStatsChanged(UserChangeStats userChangeStats) {
        var eventPayload = new UserChangeStatsChangedEventPayload(
                userChangeStats.getStartTimestamp().toString(),
                userChangeStats.getDurationInMinutes(),
                userChangeStats.getChangesCount(),
                userChangeStats.getUserId()
        );

        realtimeNotifier.sendEvent(new Event("UserChangeStatsChanged", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyUserChangeAggregateStatsChanged(UserChangeAggregateStats userChangeAggregateStats) {
        var eventPayload = new UserChangeAggregateStatsChangedEventPayload(
                userChangeAggregateStats.getUserId(),
                userChangeAggregateStats.getAddCount(),
                userChangeAggregateStats.getEditCount()
        );

        realtimeNotifier.sendEvent(new Event("UserChangeAggregateStatsChanged", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyUserWikiChangeStatsChanged(String userId, List<UserWikiChangeStatsOrdered> userWikiChangeStats) {
        var items = userWikiChangeStats.stream()
                .map(stats -> new UserWikiChangeStatsChangedEventItemPayload(
                        stats.changesCount(),
                        stats.wikiName()
                ))
                .toList();

        var eventPayload = new UserWikiChangeStatsChangedEventPayload(userId, items);

        realtimeNotifier.sendEvent(new Event("UserWikiChangeStatsChanged", eventPayload));

        return Mono.empty();
    }
}
