package application.crud.users.topusers;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUsersRepository {
    Mono<List<TopUsers>> insertAndReturnOrderedByInterval(String userName, Long changesCount, TopUsersInterval interval);
}