package domain.wiki;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IWikiEventsRealtimeNotifier {
    Mono<Void> notifyWikiCreated(Wiki wiki);
    Mono<Void> notifyTopWikisChanged(List<Wiki> topWikis);
}
