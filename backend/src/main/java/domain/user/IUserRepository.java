package domain.user;

import reactor.core.publisher.Mono;

public interface IUserRepository {
    Mono<User> getByName(String name);
    Mono<User> add(User user);
}
