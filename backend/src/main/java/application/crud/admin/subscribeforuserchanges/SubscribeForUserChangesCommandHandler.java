package application.crud.admin.subscribeforuserchanges;

import application.crud.admin.session.ISessionRepository;
import application.crud.contracts.ICommandHandler;
import domain.user.IUserRepository;
import domain.user.User;
import domain.userwiki.ITopUserWikiRepository;
import domain.userwiki.IUserWikiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class SubscribeForUserChangesCommandHandler implements ICommandHandler<SubscribeForUserChangesCommand, Void> {
    private final ISessionRepository sessionRepository;
    private final IUserRepository userRepository;
    private final IUserWikiRepository userWikiRepository;
    private final ITopUserWikiRepository topUserWikiRepository;

    @Autowired
    public SubscribeForUserChangesCommandHandler(
            ISessionRepository sessionRepository,
            IUserRepository userRepository,
            IUserWikiRepository userWikiRepository,
            ITopUserWikiRepository topUserWikiRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.userWikiRepository = userWikiRepository;
        this.topUserWikiRepository = topUserWikiRepository;
    }

    @Override
    public Mono<Void> execute(SubscribeForUserChangesCommand query) {
        return userRepository
                .getOne(
                    query(where("name").is(query.userName()))
                )
                .delayUntil(user -> {
                    sessionRepository.subscribeForUserChanges(user.getId());
                    return Mono.empty();
                })
                .delayUntil(this::initializeTopUserWikiRepository)
                .then(Mono.empty());
    }

    private Mono<Void> initializeTopUserWikiRepository(User user) {
        return userWikiRepository
                .getAll(
                        query(where("user_id").is(user.getId()))
                            .sort(Sort.by("changes_count").descending())
                            .limit(10)
                )
                .collectList()
                .flatMap(userWikis -> topUserWikiRepository.setAndReturnOrdered(user.getId(), userWikis))
                .then(Mono.empty());
    }
}