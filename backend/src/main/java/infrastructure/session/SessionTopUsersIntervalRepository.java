package infrastructure.session;

import application.admin.session.ISessionTopUsersIntervalRepository;
import application.users.topusers.TopUsersInterval;

import java.util.concurrent.atomic.AtomicInteger;

public class SessionTopUsersIntervalRepository implements ISessionTopUsersIntervalRepository {
    private final AtomicInteger interval = new AtomicInteger(TopUsersInterval.YEAR.ordinal());

    @Override
    public void setTopUsersInterval(TopUsersInterval interval) {
        this.interval.set(interval.ordinal());
    }

    @Override
    public TopUsersInterval getTopUsersInterval() {
        return TopUsersInterval.values()[interval.get()];
    }
}
