package application.streaming.changes.steps.createchange;

import domain.change.Change;
import reactor.core.publisher.Mono;

public interface IChangeRepository {
    Mono<Change> add(Change change);
}
