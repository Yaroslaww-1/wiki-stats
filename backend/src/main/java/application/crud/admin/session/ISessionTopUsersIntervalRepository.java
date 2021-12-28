package application.crud.admin.session;

import domain.user.TopUsersInterval;

public interface ISessionTopUsersIntervalRepository {
    void setTopUsersInterval(TopUsersInterval range);
    TopUsersInterval getTopUsersInterval();
}
