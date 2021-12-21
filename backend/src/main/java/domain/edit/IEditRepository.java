package domain.edit;

import reactor.core.publisher.Mono;

public interface IEditRepository {
    Mono<Edit> add(Edit edit);
}
