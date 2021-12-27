package application.users;

import domain.user.UserChangeAggregateStats;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserChangeAggregateStatsRepository {
    Flux<UserChangeAggregateStats> getAll(Query query);
    Mono<UserChangeAggregateStats> getOne(Query query);
    Mono<UserChangeAggregateStats> add(UserChangeAggregateStats userChangeAggregateStats);
    Mono<UserChangeAggregateStats> update(UserChangeAggregateStats userChangeAggregateStats);
}