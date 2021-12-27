package application.crud.users.subscribeduser;

import application.streaming.changes.steps.updateuserwikichangestats.TopUserWiki;
import domain.change.Change;
import domain.user.UserChangeAggregateStats;
import domain.user.UserChangeStats;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ISubscribedUserEventsRealtimeNotifier {
    Mono<Void> notifyChangeCreated(Change change);
    Mono<Void> notifyStatsChanged(UserChangeStats stats);
    Mono<Void> notifyAggregateStatsChanged(UserChangeAggregateStats aggregateStats);
    Mono<Void> notifyTopWikisChanged(String userId, List<TopUserWiki> topUserWikis);
}
