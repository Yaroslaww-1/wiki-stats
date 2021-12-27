package application.crud.admin.session;

import application.crud.users.topusers.TopUsersInterval;

public interface ISessionTopUsersIntervalRepository {
    void setTopUsersInterval(TopUsersInterval range);
    TopUsersInterval getTopUsersInterval();
}
