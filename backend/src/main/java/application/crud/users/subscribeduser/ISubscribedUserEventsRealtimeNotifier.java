package application.crud.users.subscribeduser;

import domain.change.Change;
import domain.user.User;
import domain.userchangesinterval.UserChangesInterval;
import domain.userwiki.UserWiki;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ISubscribedUserEventsRealtimeNotifier {
    Mono<Void> notifyChangeCreated(Change change);
    Mono<Void> notifyUserChanged(User user);
    Mono<Void> notifyLatestChangesIntervalChanged(UserChangesInterval userChangesInterval);
    Mono<Void> notifyTopWikisChanged(String userId, List<UserWiki> topUserWikis);
}
