package application.admin.session;

import application.users.topusers.TopUsersInterval;

public interface ISessionTopUsersIntervalRepository {
    void setTopUsersInterval(TopUsersInterval range);
    TopUsersInterval getTopUsersInterval();
}
