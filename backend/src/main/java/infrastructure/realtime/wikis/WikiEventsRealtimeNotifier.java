package infrastructure.realtime.wikis;

import application.wikis.IWikiEventsRealtimeNotifier;
import domain.wiki.Wiki;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class WikiEventsRealtimeNotifier implements IWikiEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public WikiEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyWikiCreated(Wiki wiki) {
        var eventPayload = new WikiCreatedEventPayload(
                wiki.getId(),
                wiki.getName()
        );

        realtimeNotifier.sendEvent(new Event("WikiCreated", eventPayload));

        return Mono.empty();
    }
}
