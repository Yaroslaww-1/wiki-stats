package application.edits;

import domain.edit.Edit;
import reactor.core.publisher.Mono;

public interface IEditRepository {
    Mono<Edit> add(Edit edit);
}
