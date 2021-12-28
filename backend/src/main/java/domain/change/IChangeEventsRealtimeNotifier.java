package domain.change;

import domain.change.Change;
import reactor.core.publisher.Mono;

public interface IChangeEventsRealtimeNotifier {
    Mono<Void> notifyChangeCreated(Change change);
}
