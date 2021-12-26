package application.changes;

import domain.change.Change;
import domain.user.UserChangeStats;
import reactor.core.publisher.Mono;

public interface IChangeEventsRealtimeNotifier {
    Mono<Void> notifyChangeCreated(Change change);
    Mono<Void> notifySubscribedUserChangeCreated(Change change);
    Mono<Void> notifyChangeStatsChanged(UserChangeStats userChangeStats);
}
