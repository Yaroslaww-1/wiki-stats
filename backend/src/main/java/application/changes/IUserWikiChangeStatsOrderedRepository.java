package application.changes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserWikiChangeStatsOrderedRepository {
    Flux<UserWikiChangeStatsOrdered> insertAndReturnOrdered(String userId, UserWikiChangeStatsOrdered userWikiChangeStats);
    Mono<Void> set(String userId, List<UserWikiChangeStatsOrdered> userWikiChangeStats);
}
