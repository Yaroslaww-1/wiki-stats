package infrastructure.session;

import application.admin.session.IUserSubscriptionRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SessionSubscriptionRepository implements IUserSubscriptionRepository {
    private final List<String> subscriptions = new CopyOnWriteArrayList<String>();

    public void subscribeForUserChanges(String userId) {
        subscriptions.add(userId);
    }

    public Boolean isSubscribedForUserChanges(String userId) {
        return subscriptions.contains(userId);
    }

    public void unsubscribeAll() {
        subscriptions.clear();
    }
}
