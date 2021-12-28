package domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserEventsRealtimeNotifier {
    Mono<Void> notifyUserCreated(User user);
    Mono<Void> notifyTopUsersChanged(List<User> topUsers);
}
