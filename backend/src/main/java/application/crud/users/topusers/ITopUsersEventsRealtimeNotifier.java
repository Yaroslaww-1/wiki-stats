package application.crud.users.topusers;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUsersEventsRealtimeNotifier {
    Mono<Void> notifyTopUsersChanged(List<TopUsers> topUsers);
}
