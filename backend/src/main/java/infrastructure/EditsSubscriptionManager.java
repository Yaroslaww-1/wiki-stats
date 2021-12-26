package infrastructure;

import application.users.IEditsSubscriptionManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class EditsSubscriptionManager implements IEditsSubscriptionManager {
    private final List<String> subscriptions = new CopyOnWriteArrayList<String>();

    @Override
    public void subscribeForUserEdits(String userId) {
        subscriptions.add(userId);
    }

    @Override
    public Boolean isSubscribedForUserEdits(String userId) {
        return subscriptions.contains(userId);
    }

    @Override
    public void unsubscribeAll() {
        subscriptions.clear();
    }
}
