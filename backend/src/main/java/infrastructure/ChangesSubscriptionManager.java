package infrastructure;

import application.users.IChangesSubscriptionManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChangesSubscriptionManager implements IChangesSubscriptionManager {
    private final List<String> subscriptions = new CopyOnWriteArrayList<String>();

    @Override
    public void subscribeForUserChanges(String userId) {
        subscriptions.add(userId);
    }

    @Override
    public Boolean isSubscribedForUserChanges(String userId) {
        return subscriptions.contains(userId);
    }

    @Override
    public void unsubscribeAll() {
        subscriptions.clear();
    }
}
