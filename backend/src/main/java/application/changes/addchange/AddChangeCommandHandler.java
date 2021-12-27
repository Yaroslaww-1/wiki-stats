package application.changes.addchange;

import application.admin.session.ISessionRepository;
import application.changes.*;
import application.contracts.ICommandHandler;
import application.users.*;
import application.users.topusers.ITopUsersEventsRealtimeNotifier;
import application.users.topusers.ITopUsersRepository;
import application.wikis.IWikiEventsRealtimeNotifier;
import domain.change.Change;
import domain.user.User;
import application.wikis.IWikiRepository;
import domain.user.UserChangeAggregateStats;
import domain.user.UserChangeStats;
import domain.user.UserWikiChangeStats;
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
    private final IUserChangeStatsRepository userChangeStatsRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;
    private final IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier;
    private final IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier;
    private final ISessionRepository sessionRepository;
    private final IUserWikiChangeStatsRepository userWikiChangeStatsRepository;
    private final IUserWikiChangeStatsOrderedRepository userWikiChangeStatsOrderedRepository;
    private final IUserChangeAggregateStatsRepository userChangeAggregateStatsRepository;
    private final ITopUsersRepository topUsersRepository;
    private final ITopUsersEventsRealtimeNotifier topUsersEventsRealtimeNotifier;

    @Autowired
    public AddChangeCommandHandler(
            IUserRepository userRepository,
            IWikiRepository wikiRepository,
            IChangeRepository changeRepository,
            IUserChangeStatsRepository userChangeStatsRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier,
            IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier,
            IChangeEventsRealtimeNotifier changeEventsRealtimeNotifier,
            ISessionRepository sessionRepository,
            IUserWikiChangeStatsRepository userWikiChangeStatsRepository,
            IUserWikiChangeStatsOrderedRepository userWikiChangeStatsOrderedRepository,
            IUserChangeAggregateStatsRepository userChangeAggregateStatsRepository,
            ITopUsersRepository topUsersRepository,
            ITopUsersEventsRealtimeNotifier topUsersEventsRealtimeNotifier
    ) {
        this.userRepository = userRepository;
        this.wikiRepository = wikiRepository;
        this.changeRepository = changeRepository;
        this.userChangeStatsRepository = userChangeStatsRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
        this.wikiEventsRealtimeNotifier = wikiEventsRealtimeNotifier;
        this.changeEventsRealtimeNotifier = changeEventsRealtimeNotifier;
        this.sessionRepository = sessionRepository;
        this.userWikiChangeStatsRepository = userWikiChangeStatsRepository;
        this.userWikiChangeStatsOrderedRepository = userWikiChangeStatsOrderedRepository;
        this.userChangeAggregateStatsRepository = userChangeAggregateStatsRepository;
        this.topUsersRepository = topUsersRepository;
        this.topUsersEventsRealtimeNotifier = topUsersEventsRealtimeNotifier;
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
                .delayUntil(this::createOrUpdateUserChangeStats)
                .delayUntil(this::createOrUpdateUserWikiChangeStats)
                .delayUntil(this::updateUserChangeAggregateStats);
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
                .delayUntil(user -> userChangeAggregateStatsRepository.add(
                        new UserChangeAggregateStats(user.getId())
                ))
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
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getEditor().getId()))
                .delayUntil(changeEventsRealtimeNotifier::notifySubscribedUserChangeCreated);
    }

    private Mono<UserChangeStats> createOrUpdateUserChangeStats(Change change) {
        var windowInMinutes = 1L;

        return userChangeStatsRepository
                .getOne(
                        query(where("user_id").is(change.getEditor().getId()))
                )
                .filter(userChangeStats ->
                        userChangeStats.getStartTimestamp().plusMinutes(windowInMinutes).isAfter(LocalDateTime.now())
                )
                .switchIfEmpty(userChangeStatsRepository.add(
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
                .delayUntil(userChangeStatsRepository::update)
                .delayUntil(this::notifySubscribedUserChangeStatsChanged)
                .delayUntil(stats -> topUsersRepository
                        .insertAndReturnOrdered(change.getEditor().getName(), stats.getChangesCount())
                        .collectList()
                        .delayUntil(topUsersEventsRealtimeNotifier::notifyTopUsersChanged)
                );
    }

    private Mono<UserChangeStats> notifySubscribedUserChangeStatsChanged(UserChangeStats userChangeStats) {
        return Mono.just(userChangeStats)
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getUserId()))
                .delayUntil(changeEventsRealtimeNotifier::notifyUserChangeStatsChanged);
    }

    private Mono<Void> createOrUpdateUserWikiChangeStats(Change change) {
        if (!sessionRepository.isSubscribedForUserChanges(change.getEditor().getId())) {
            return Mono.empty();
        }

        return userWikiChangeStatsRepository
                .getOne(
                        query(
                                where("user_id").is(change.getEditor().getId()).and(
                                where("wiki_id").is(change.getWiki().getId()))
                        )
                )
                .switchIfEmpty(userWikiChangeStatsRepository.add(
                        new UserWikiChangeStats(0L, change.getEditor().getId(), change.getWiki().getId())
                ))
                .delayUntil(userWikiChangeStats -> {
                    if (change.isEdit() || change.isAdd()) {
                        userWikiChangeStats.incrementChanges();
                    }
                    return Mono.just(userWikiChangeStats);
                })
                .delayUntil(userWikiChangeStatsRepository::update)
                .flatMapMany(stats -> userWikiChangeStatsOrderedRepository.insertAndReturnOrdered(
                        change.getEditor().getId(),
                        new UserWikiChangeStatsOrdered(stats.getChangesCount(), change.getWiki().getName())
                ))
                .collectList()
                .delayUntil(statsList -> changeEventsRealtimeNotifier.notifyUserWikiChangeStatsChanged(
                        change.getEditor().getId(),
                        statsList
                ))
                .then(Mono.empty());
    }

    private Mono<Void> updateUserChangeAggregateStats(Change change) {
        return userChangeAggregateStatsRepository
                .getOne(
                        query(where("user_id").is(change.getEditor().getId()))
                )
                .delayUntil(userChangeAggregateStats -> {
                    if (change.isEdit()) {
                        userChangeAggregateStats.incrementEdits();
                    }

                    if (change.isAdd()) {
                        userChangeAggregateStats.incrementAdds();
                    }

                    return Mono.just(userChangeAggregateStats);
                })
                .delayUntil(userChangeAggregateStatsRepository::update)
                .filter(c -> sessionRepository.isSubscribedForUserChanges(c.getUserId()))
                .delayUntil(changeEventsRealtimeNotifier::notifyUserChangeAggregateStatsChanged)
                .then(Mono.empty());
    }
}
