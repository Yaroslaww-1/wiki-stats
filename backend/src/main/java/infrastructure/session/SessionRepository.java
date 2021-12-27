package infrastructure.session;

import application.crud.admin.session.ISessionRepository;
import application.crud.users.topusers.TopUsersInterval;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SessionRepository implements ISessionRepository {
    private final SessionSubscriptionRepository sessionSubscriptionRepository = new SessionSubscriptionRepository();
    private final SessionChangesProcessingDelayRepository sessionChangesProcessingDelayRepository = new SessionChangesProcessingDelayRepository();
    private final SessionTopUsersIntervalRepository sessionTopUsersIntervalRepository = new SessionTopUsersIntervalRepository();

    // Processing Delay
    @Override
    public void setChangesProcessingDelay(Duration delay) {
        sessionChangesProcessingDelayRepository.setChangesProcessingDelay(delay);
    }

    @Override
    public Duration getProcessingDelay() {
        return sessionChangesProcessingDelayRepository.getProcessingDelay();
    }

    // Top Users Interval
    @Override
    public void setTopUsersInterval(TopUsersInterval interval) {
        sessionTopUsersIntervalRepository.setTopUsersInterval(interval);
    }

    @Override
    public TopUsersInterval getTopUsersInterval() {
        return sessionTopUsersIntervalRepository.getTopUsersInterval();
    }

    // Subscriptions
    @Override
    public void subscribeForUserChanges(String userId) {
        sessionSubscriptionRepository.subscribeForUserChanges(userId);
    }

    @Override
    public Boolean isSubscribedForUserChanges(String userId) {
        return sessionSubscriptionRepository.isSubscribedForUserChanges(userId);
    }

    @Override
    public void unsubscribeAll() {
        sessionSubscriptionRepository.unsubscribeAll();
    }
}
