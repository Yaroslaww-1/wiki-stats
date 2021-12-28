package domain.user;

import domain.userwiki.UserWiki;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUserRepository {
    Mono<List<User>> insertAndReturnOrderedByInterval(User topUser, TopUsersInterval interval);
    Mono<List<User>> setAndReturnOrdered(List<User> users, TopUsersInterval interval);
}