package application.users;

import domain.user.UserEditStats;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserEditStatsEntityRepository {
    Flux<UserEditStats> getAll(Query query);
    Mono<UserEditStats> getOne(Query query);
    Mono<UserEditStats> add(UserEditStats userEditStats);
    Mono<UserEditStats> update(UserEditStats userEditStats);
}