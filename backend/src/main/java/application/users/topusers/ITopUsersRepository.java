package application.users.topusers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITopUsersRepository {
    Flux<TopUsers> insertAndReturnOrderedByInterval(String userName, Long changesCount, TopUsersInterval interval);
}