package domain.wiki;

import reactor.core.publisher.Mono;

public interface IWikiRepository {
    Mono<Wiki> getByName(String name);
    Mono<Wiki> add(Wiki wiki);
}
