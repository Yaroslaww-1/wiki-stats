package application.streaming.changes.steps.createchange;

import domain.change.Change;
import reactor.core.publisher.Mono;

public interface IChangeEventsRealtimeNotifier {
    Mono<Void> notifyChangeCreated(Change change);
}
