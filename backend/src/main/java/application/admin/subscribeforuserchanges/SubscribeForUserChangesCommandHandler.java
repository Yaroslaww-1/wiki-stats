package application.admin.subscribeforuserchanges;

import application.admin.session.ISessionRepository;
import application.contracts.ICommandHandler;
import application.users.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class SubscribeForUserChangesCommandHandler implements ICommandHandler<SubscribeForUserChangesCommand, Void> {
    private final ISessionRepository sessionRepository;
    private final IUserRepository userRepository;

    @Autowired
    public SubscribeForUserChangesCommandHandler(
            ISessionRepository sessionRepository,
            IUserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Void> execute(SubscribeForUserChangesCommand query) {
        return userRepository
                .getOne(
                    query(where("name").is(query.userName()))
                )
                .flatMap(user -> {
                    sessionRepository.subscribeForUserChanges(user.getId());
                    return Mono.empty();
                });
    }
}