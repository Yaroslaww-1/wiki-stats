package application.users;

import domain.user.User;
import reactor.core.publisher.Mono;

public interface IUserEventsRealtimeNotifier {
    Mono<Void> notifyUserCreated(User user);
}
