package application.streaming.changes.flows.initializecaches;

import application.streaming.contracts.IMonoFlow;
import domain.user.ITopUserRepository;
import domain.user.IUserRepository;
import domain.user.TopUsersInterval;
import domain.wiki.ITopWikiRepository;
import domain.wiki.IWikiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class InitializeCachesFlow implements IMonoFlow<InitializeCachesFlowInput, Void> {
    private final ITopUserRepository topUserRepository;
    private final ITopWikiRepository topWikiRepository;

    private final IUserRepository userRepository;
    private final IWikiRepository wikiRepository;

    @Autowired
    public InitializeCachesFlow(
            ITopUserRepository topUserRepository,
            ITopWikiRepository topWikiRepository,
            IUserRepository userRepository,
            IWikiRepository wikiRepository
    ) {
        this.topUserRepository = topUserRepository;
        this.topWikiRepository = topWikiRepository;
        this.userRepository = userRepository;
        this.wikiRepository = wikiRepository;
    }

    @Override
    public Mono<Void> run(InitializeCachesFlowInput input) {
        return Mono
                .zip(
                        this.initializeTopUserRepository(),
                        this.initializeTopWikiRepository()
                )
                .then(Mono.empty());
    }

    private Mono<Void> initializeTopUserRepository() {
        return userRepository
                .getAll(
                        Query.empty()
                                .sort(Sort.by("edits_count").descending())
                                .limit(10)
                )
                .collectList()
                .flatMap(users -> Mono
                        .zip(
                            topUserRepository.setAndReturnOrdered(users, TopUsersInterval.DAY), //TODO: actually filter by date range
                            topUserRepository.setAndReturnOrdered(users, TopUsersInterval.MONTH),
                            topUserRepository.setAndReturnOrdered(users, TopUsersInterval.YEAR)
                        )
                )
                .then(Mono.empty());
    }

    private Mono<Void> initializeTopWikiRepository() {
        return wikiRepository
                .getAll(
                        Query.empty()
                                .sort(Sort.by("edits_count").descending())
                                .limit(10)
                )
                .collectList()
                .flatMap(topWikiRepository::setAndReturnOrdered)
                .then(Mono.empty());
    }

}