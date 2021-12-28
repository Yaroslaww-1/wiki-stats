package domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUserRepository {
    Mono<List<User>> insertAndReturnOrderedByInterval(User topUser, TopUsersInterval interval);
}