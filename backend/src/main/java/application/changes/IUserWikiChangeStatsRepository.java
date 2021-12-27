package application.changes;

import domain.user.UserWikiChangeStats;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserWikiChangeStatsRepository {
    Flux<UserWikiChangeStats> getAll(Query query);
    Mono<UserWikiChangeStats> getOne(Query query);
    Mono<UserWikiChangeStats> add(UserWikiChangeStats userWikiChangeStats);
    Mono<UserWikiChangeStats> update(UserWikiChangeStats userWikiChangeStats);
}