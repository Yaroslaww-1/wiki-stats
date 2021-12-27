package application.crud.wikis;

import domain.wiki.Wiki;
import reactor.core.publisher.Mono;

public interface IWikiEventsRealtimeNotifier {
    Mono<Void> notifyWikiCreated(Wiki wiki);
}
