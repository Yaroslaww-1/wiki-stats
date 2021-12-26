package application.changes.addchange;

import application.contracts.ICommandHandler;
import application.changes.IChangeEventsRealtimeNotifier;
import application.users.IChangesSubscriptionManager;
import application.users.IUserChangeStatsEntityRepository;
import application.users.IUserEventsRealtimeNotifier;
import application.wikis.IWikiEventsRealtimeNotifier;
import domain.change.Change;
import application.changes.IChangeRepository;
import application.users.IUserRepository;
import domain.user.User;
import application.wikis.IWikiRepository;
import domain.user.UserChangeStats;
import domain.wiki.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.Random;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class AddChangeCommandHandler implements ICommandHandler<AddChangeCommand, Change> {
    private final IUserRepository userRepository;
    private final IWikiRepository wikiRepository;
    private final IChangeRepository changeRepository;
    private final IUserChangeStatsEntityRepository userChangeStatsEntityRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;
    private final IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier;
    private final IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier;
    private final IChangesSubscriptionManager changesSubscriptionManager;

    @Autowired
    public AddChangeCommandHandler(
            IUserRepository userRepository,
            IWikiRepository wikiRepository,
            IChangeRepository changeRepository,
            IUserChangeStatsEntityRepository userChangeStatsEntityRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier,
            IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier,
            IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier,
            IChangesSubscriptionManager changesSubscriptionManager) {
        this.userRepository = userRepository;
        this.wikiRepository = wikiRepository;
        this.changeRepository = changeRepository;
        this.userChangeStatsEntityRepository = userChangeStatsEntityRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
        this.wikiEventsRealtimeNotifier = wikiEventsRealtimeNotifier;
        this.changeEventsRealtimeNotifier = changeEventsRealtimeNotifier;
        this.changesSubscriptionManager = changesSubscriptionManager;
    }

    @Override
    public Mono<Change> execute(AddChangeCommand command) {
        var editor = getEditorName(command.editor());

        var userMono = userRepository.getOne(
                    query(where("name").is(editor))
                )
                .switchIfEmpty(this.createUser(editor, command.isBot()));

        var wikiMono = wikiRepository.getOne(
                    query(where("name").is(command.wiki()))
                )
                .switchIfEmpty(this.createWiki(command.wiki()));

        return Mono.zip(userMono, wikiMono)
                .flatMap(userWikiTuple -> this.createChange(
                        userWikiTuple,
                        command.id(),
                        command.timestamp(),
                        command.title(),
                        command.comment(),
                        command.type()
                ))
                .delayUntil(this::createOrUpdateUserChangeStats);
    }

    /**
     * With 10% chance replaces initial editor name by hardcoded one, so we can test further functionality more easily
     * @param commandEditorName
     * @return Editor name from command of hardcoded editor name
     */
    private String getEditorName(String commandEditorName) {
        Random rand = new Random();
        if (rand.nextInt(100) <= 10) {
            return "Kappa";
        } else {
            return commandEditorName;
        }
    }

    private Mono<Change> createChange(
            Tuple2<User, Wiki> userWikiTuple,
            String id,
            LocalDateTime timestamp,
            String title,
            String comment,
            String type
    ) {
        var user = userWikiTuple.getT1();
        var wiki = userWikiTuple.getT2();
        return Mono
                .just(new Change(id, timestamp, title, comment, type, user, wiki))
                .delayUntil(changeRepository::add)
                .delayUntil(changeEventsRealtimeNotifier::notifyChangeCreated)
                .delayUntil(this::notifySubscribedUserChangeCreated);
    }

    private Mono<User> createUser(String editor, Boolean isBot) {
        return Mono
                .just(new User(editor, isBot))
                .delayUntil(userRepository::add)
                .delayUntil(userEventsRealtimeNotifier::notifyUserCreated);
    }

    private Mono<Wiki> createWiki(String name) {
        return Mono
                .just(new Wiki(name))
                .delayUntil(wikiRepository::add)
                .delayUntil(wikiEventsRealtimeNotifier::notifyWikiCreated);
    }

    private Mono<Change> notifySubscribedUserChangeCreated(Change change) {
        return Mono.just(change)
                .filter(c -> changesSubscriptionManager.isSubscribedForUserChanges(c.getEditor().getId()))
                .delayUntil(changeEventsRealtimeNotifier::notifySubscribedUserChangeCreated);
    }

    private Mono<UserChangeStats> createOrUpdateUserChangeStats(Change change) {
        var windowInMinutes = 1L;

        return userChangeStatsEntityRepository
                .getOne(
                        query(where("user_id").is(change.getEditor().getId()))
                )
                .filter(userChangeStats ->
                        userChangeStats.getStartTimestamp().plusMinutes(windowInMinutes).isAfter(LocalDateTime.now())
                )
                .switchIfEmpty(userChangeStatsEntityRepository.add(
                        new UserChangeStats(change.getEditor().getId(), windowInMinutes)
                ))
                .delayUntil(userChangeStats -> {
                    if (change.isEdit()) {
                        userChangeStats.incrementEdits();
                    }

                    if (change.isAdd()) {
                        userChangeStats.incrementAdds();
                    }

                    return Mono.just(userChangeStats);
                })
                .delayUntil(userChangeStatsEntityRepository::update)
                .delayUntil(this::notifySubscribedUserChangeStatsChanged);
    }

    private Mono<UserChangeStats> notifySubscribedUserChangeStatsChanged(UserChangeStats userChangeStats) {
        return Mono.just(userChangeStats)
                .filter(c -> changesSubscriptionManager.isSubscribedForUserChanges(c.getUserId()))
                .delayUntil(changeEventsRealtimeNotifier::notifyChangeStatsChanged);
    }
}
