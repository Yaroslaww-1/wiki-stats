package infrastructure.realtime;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class RealtimeNotifier implements IRealtimeNotifier {
    private final Sinks.Many<Event> processor = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void sendEvent(Event next) {
        processor.tryEmitNext(next);
    }

    @Override
    public Flux<Event> getEvents() {
        return processor.asFlux();
    }
}