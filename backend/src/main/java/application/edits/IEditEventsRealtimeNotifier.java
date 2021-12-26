package application.edits;

import domain.edit.Edit;
import domain.user.UserEditStats;
import reactor.core.publisher.Mono;

public interface IEditEventsRealtimeNotifier {
    Mono<Void> notifyEditCreated(Edit edit);
    Mono<Void> notifySubscribedUserEditCreated(Edit edit);
    Mono<Void> notifyEditStatsChanged(UserEditStats userEditStats);
}
