package infrastructure.realtime.users.topusers;

import application.crud.users.topusers.ITopUsersEventsRealtimeNotifier;
import application.crud.users.topusers.TopUsers;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TopUsersEventsRealtimeNotifier implements ITopUsersEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public TopUsersEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyTopUsersChanged(List<TopUsers> topUsers) {
        var items = topUsers.stream()
                .map(stats -> new TopUsersEventPayload(
                        stats.userName(),
                        stats.changesCount()
                ))
                .toList();

        var eventPayload = new TopUsersChangedEventPayload(items);

        realtimeNotifier.sendEvent(new Event("TopUsersChanged", eventPayload));

        return Mono.empty();
    }
}
