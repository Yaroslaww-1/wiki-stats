package domain.userwiki;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUserWikiRepository {
    Mono<List<UserWiki>> insertAndReturnOrdered(String userId, UserWiki userWiki);
}
