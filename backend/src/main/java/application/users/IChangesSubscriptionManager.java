package application.users;

public interface IChangesSubscriptionManager {
    void subscribeForUserChanges(String userId);
    Boolean isSubscribedForUserChanges(String userId);
    void unsubscribeAll();
    // To support multiple clients connected to WS we probably need something like that:
    // Flux<Client> getAllClientsSubscribedForUserChanges(String userId);
}
