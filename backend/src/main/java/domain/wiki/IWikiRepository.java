package domain.wiki;

import domain.wiki.Wiki;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWikiRepository {
    Flux<Wiki> getAll(Query query);
    Mono<Wiki> getOne(Query query);
    Mono<Long> getTotalCount();
    Mono<Wiki> add(Wiki wiki);
    Mono<Wiki> update(Wiki wiki);
}
