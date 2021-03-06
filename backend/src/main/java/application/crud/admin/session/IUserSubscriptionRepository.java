package application.crud.admin.session;

public interface IUserSubscriptionRepository {
    void subscribeForUserChanges(String userId);
    Boolean isSubscribedForUserChanges(String userId);
    void unsubscribeAll();
}
