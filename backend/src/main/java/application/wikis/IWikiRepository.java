package application.wikis;

import domain.wiki.Wiki;
import reactor.core.publisher.Mono;

public interface IWikiRepository {
    Mono<Wiki> getByName(String name);
    Mono<Wiki> add(Wiki wiki);
}
