package infrastructure.realtime.users;

import application.crud.users.IUserEventsRealtimeNotifier;
import domain.user.User;
import infrastructure.realtime.Event;
import infrastructure.realtime.IRealtimeNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
}
