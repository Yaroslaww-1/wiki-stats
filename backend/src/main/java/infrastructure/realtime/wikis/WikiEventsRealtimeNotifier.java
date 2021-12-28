package infrastructure.realtime.wikis;

import domain.wiki.IWikiEventsRealtimeNotifier;
import domain.wiki.Wiki;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import infrastructure.realtime.users.TopUsersChangedEventPayload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<Void> notifyTopWikisChanged(List<Wiki> topWikis) {
        var items = topWikis.stream()
                .map(wiki -> new TopWikisChangedEventPayloadWiki(
                        wiki.getName(),
                        wiki.getEditsCount()
                ))
                .toList();

        var eventPayload = new TopWikisChangedEventPayload(items);

        realtimeNotifier.sendEvent(new Event("TopWikisChanged", eventPayload));

        return Mono.empty();
    }
}
