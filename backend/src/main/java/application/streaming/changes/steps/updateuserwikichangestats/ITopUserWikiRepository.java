package application.streaming.changes.steps.updateuserwikichangestats;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUserWikiRepository {
    Mono<List<TopUserWiki>> insertAndReturnOrdered(String userId, TopUserWiki userWiki);
    Mono<Void> set(String userId, List<TopUserWiki> userWikis);
}
