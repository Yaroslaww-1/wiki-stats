package application.changes;

import domain.change.Change;
import reactor.core.publisher.Mono;

public interface IChangeRepository {
    Mono<Change> add(Change change);
}
