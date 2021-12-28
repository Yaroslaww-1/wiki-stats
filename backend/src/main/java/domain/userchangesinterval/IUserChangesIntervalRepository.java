package domain.userchangesinterval;

import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserChangesIntervalRepository {
    Flux<UserChangesInterval> getAll(Query query);
    Mono<UserChangesInterval> getOne(Query query);
    Mono<UserChangesInterval> add(UserChangesInterval userChangesInterval);
    Mono<UserChangesInterval> update(UserChangesInterval userChangesInterval);
}