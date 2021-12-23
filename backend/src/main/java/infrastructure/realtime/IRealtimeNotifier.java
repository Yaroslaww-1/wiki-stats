package infrastructure.realtime;

import reactor.core.publisher.Flux;

public interface IRealtimeNotifier {
    void sendEvent(Event next);
    Flux<Event> getEvents();
}
