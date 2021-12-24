package application.edits;

import domain.edit.Edit;
import reactor.core.publisher.Mono;

public interface IEditEventsRealtimeNotifier {
    Mono<Void> notifyEditCreated(Edit edit);
}
