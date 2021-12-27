package application.crud.admin.session;

//TODO: these repositories can be adjusted for multi-session usages by adding userId param to methods
public interface ISessionRepository extends
        ISessionChangesProcessingDelayRepository,
        ISessionTopUsersIntervalRepository,
        IUserSubscriptionRepository
{ }
