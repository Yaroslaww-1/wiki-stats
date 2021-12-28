package domain.userwiki;

import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserWikiRepository {
    Flux<UserWiki> getAll(Query query);
    Mono<UserWiki> getOne(Query query);
    Mono<UserWiki> add(UserWiki userWiki);
    Mono<UserWiki> update(UserWiki userWiki);
}