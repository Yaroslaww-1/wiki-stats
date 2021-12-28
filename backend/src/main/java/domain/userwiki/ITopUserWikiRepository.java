package domain.userwiki;

import domain.wiki.Wiki;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUserWikiRepository {
    Mono<List<UserWiki>> insertAndReturnOrdered(String userId, UserWiki userWiki);
    Mono<List<UserWiki>> setAndReturnOrdered(String userId, List<UserWiki> userWikis);
}
