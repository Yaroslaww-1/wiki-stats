package application.changes;

import domain.change.Change;
import domain.user.UserChangeAggregateStats;
import domain.user.UserChangeStats;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IChangeEventsRealtimeNotifier {
    Mono<Void> notifyChangeCreated(Change change);
    Mono<Void> notifySubscribedUserChangeCreated(Change change);
    Mono<Void> notifyUserChangeStatsChanged(UserChangeStats userChangeStats);
    Mono<Void> notifyUserChangeAggregateStatsChanged(UserChangeAggregateStats userChangeAggregateStats);
    Mono<Void> notifyUserWikiChangeStatsChanged(String userId, List<UserWikiChangeStatsOrdered> userWikiChangeStats);
}
