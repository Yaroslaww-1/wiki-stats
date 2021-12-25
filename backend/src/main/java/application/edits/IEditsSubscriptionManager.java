package application.edits;

public interface IEditsSubscriptionManager {
    void subscribeForUserEdits(String userId);
    Boolean isSubscribedForUserEdits(String userId);
    // To support multiple clients connected to WS we probably need something like that:
    // Flux<Client> getAllClientsSubscribedForUserEdits(String userId);
}