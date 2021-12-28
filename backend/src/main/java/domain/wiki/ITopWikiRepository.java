package domain.wiki;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopWikiRepository {
    Mono<List<Wiki>> insertAndReturnOrdered(Wiki wiki);
}
