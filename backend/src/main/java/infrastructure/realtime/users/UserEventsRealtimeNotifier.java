package infrastructure.realtime.users;

import domain.user.IUserEventsRealtimeNotifier;
import domain.user.User;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class UserEventsRealtimeNotifier implements IUserEventsRealtimeNotifier {
    private final IRealtimeNotifier realtimeNotifier;

    public UserEventsRealtimeNotifier(IRealtimeNotifier realtimeNotifier) {
        this.realtimeNotifier = realtimeNotifier;
    }

    @Override
    public Mono<Void> notifyUserCreated(User user) {
        var eventPayload = new UserCreatedEventPayload(
                user.getId(),
                user.getName(),
                user.getIsBot()
        );

        realtimeNotifier.sendEvent(new Event("UserCreated", eventPayload));

        return Mono.empty();
    }

    @Override
    public Mono<Void> notifyTopUsersChanged(List<User> topUsers) {
        var items = topUsers.stream()
                .map(user -> new TopUsersEventPayload(
                        user.getName(),
                        user.getChangesCount()
                ))
                .toList();

        var eventPayload = new TopUsersChangedEventPayload(items);

        realtimeNotifier.sendEvent(new Event("TopUsersChanged", eventPayload));

        return Mono.empty();
    }
}
