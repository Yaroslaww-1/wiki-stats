package application.users;

import domain.user.UserChangeStats;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserChangeStatsEntityRepository {
    Flux<UserChangeStats> getAll(Query query);
    Mono<UserChangeStats> getOne(Query query);
    Mono<UserChangeStats> add(UserChangeStats userChangeStats);
    Mono<UserChangeStats> update(UserChangeStats userChangeStats);
}