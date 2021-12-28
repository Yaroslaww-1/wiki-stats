package infrastructure.realtime.users.subscribeduser;

import application.crud.users.subscribeduser.ISubscribedUserEventsRealtimeNotifier;
import domain.change.Change;
import domain.user.User;
import domain.userchangesinterval.UserChangesInterval;
import domain.userwiki.UserWiki;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SubscribedUserEventsRealtimeNotifier implements ISubscribedUserEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public SubscribedUserEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyChangeCreated(Change change) {
        var eventPayload = new SubscribedUserChangeCreatedEventPayload(
                change.getId(),
                change.getTimestamp(),
                change.getTitle(),
                change.getComment(),
                change.getUser().getName(),
                change.getWiki().getName()
        );

        realtimeNotifier.sendEvent(new Event("SubscribedUserChangeCreated", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyUserChanged(User user) {
        var eventPayload = new SubscribedUserChangedEventPayload(
                user.getId(),
                user.getAddsCount(),
                user.getEditsCount()
        );

        realtimeNotifier.sendEvent(new Event("SubscribedUserAggregateStatsChanged", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyLatestChangesIntervalChanged(UserChangesInterval userChangesInterval) {
        var eventPayload = new SubscribedUserChangesIntervalChangedEventPayload(
                userChangesInterval.getStartTimestamp().toString(),
                userChangesInterval.getDurationInMinutes(),
                userChangesInterval.getChangesCount(),
                userChangesInterval.getUserId()
        );

        realtimeNotifier.sendEvent(new Event("SubscribedUserStatsChanged", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyTopWikisChanged(String userId, List<UserWiki> topUserWikis) {
        var items = topUserWikis.stream()
                .map(userWiki -> new SubscribedUserTopWikiChangedEventPayloadWiki(
                        userWiki.getChangesCount(),
                        userWiki.getWikiName()
                ))
                .toList();

        var eventPayload = new SubscribedUserTopWikiChangedEventPayload(userId, items);

        realtimeNotifier.sendEvent(new Event("SubscribedUserTopWikiChanged", eventPayload));

        return Mono.empty();
    }
}
