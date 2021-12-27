package infrastructure.realtime.users.subscribeduser;

import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import application.streaming.changes.steps.updateuserwikichangestats.TopUserWiki;
import domain.change.Change;
import domain.user.UserChangeAggregateStats;
import domain.user.UserChangeStats;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SubscribedUserEventsRealtimeNotifier implements ISubscribedUserEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public SubscribedUserEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyChangeCreated(Change change) {
        var eventPayload = new SubscribedUserChangeCreatedEventPayload(
                change.getId(),
                change.getTimestamp(),
                change.getTitle(),
                change.getComment(),
                change.getEditor().getName(),
                change.getWiki().getName()
        );

        realtimeNotifier.sendEvent(new Event("SubscribedUserChangeCreated", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyStatsChanged(UserChangeStats stats) {
        var eventPayload = new SubscribedUserStatsChangedEventPayload(
                stats.getStartTimestamp().toString(),
                stats.getDurationInMinutes(),
                stats.getChangesCount(),
                stats.getUserId()
        );

        realtimeNotifier.sendEvent(new Event("SubscribedUserStatsChanged", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyAggregateStatsChanged(UserChangeAggregateStats aggregateStats) {
        var eventPayload = new SubscribedUserAggregateStatsChangedEventPayload(
                aggregateStats.getUserId(),
                aggregateStats.getAddCount(),
                aggregateStats.getEditCount()
        );

        realtimeNotifier.sendEvent(new Event("SubscribedUserAggregateStatsChanged", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyTopWikisChanged(String userId, List<TopUserWiki> topUserWikis) {
        var items = topUserWikis.stream()
                .map(stats -> new SubscribedUserTopWikiChangedEventPayloadWiki(
                        stats.changesCount(),
                        stats.wikiName()
                ))
                .toList();

        var eventPayload = new SubscribedUserTopWikiChangedEventPayload(userId, items);

        realtimeNotifier.sendEvent(new Event("SubscribedUserTopWikiChanged", eventPayload));

        return Mono.empty();
    }
}
