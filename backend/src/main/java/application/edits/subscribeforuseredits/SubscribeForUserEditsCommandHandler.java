package application.edits.subscribeforuseredits;

import application.contracts.ICommandHandler;
import application.edits.IEditsSubscriptionManager;
import application.users.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class SubscribeForUserEditsCommandHandler implements ICommandHandler<SubscribeForUserEditsCommand, Void> {
    private final IEditsSubscriptionManager editsSubscriptionManager;
    private final IUserRepository userRepository;

    @Autowired
    public SubscribeForUserEditsCommandHandler(
            IEditsSubscriptionManager editsSubscriptionManager,
            IUserRepository userRepository) {
        this.editsSubscriptionManager = editsSubscriptionManager;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Void> execute(SubscribeForUserEditsCommand query) {
        return userRepository
                .getOne(
                    query(where("name").is(query.userName()))
                )
                .flatMap(user -> {
                    editsSubscriptionManager.subscribeForUserEdits(user.getId());
                    return Mono.empty();
                });
    }
}