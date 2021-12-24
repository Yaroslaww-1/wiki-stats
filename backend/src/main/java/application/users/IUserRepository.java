package application.users;

import domain.user.User;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserRepository {
    Flux<User> getAll(Query query);
    Mono<User> getOne(Query query);
    Mono<Long> getTotalCount();
    Mono<User> add(User user);
}
