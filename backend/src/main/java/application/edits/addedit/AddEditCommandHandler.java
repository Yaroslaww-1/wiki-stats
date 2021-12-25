package application.edits.addedit;

import application.contracts.ICommandHandler;
import application.edits.IEditEventsRealtimeNotifier;
import application.edits.IEditsSubscriptionManager;
import application.users.IUserEventsRealtimeNotifier;
import application.wikis.IWikiEventsRealtimeNotifier;
import domain.edit.Edit;
import application.edits.IEditRepository;
import application.users.IUserRepository;
import domain.user.User;
import application.wikis.IWikiRepository;
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
public class AddEditCommandHandler implements ICommandHandler<AddEditCommand, Edit> {
    private final IUserRepository userRepository;
    private final IWikiRepository wikiRepository;
    private final IEditRepository editRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;
    private final IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier;
    private final IEditEventsRealtimeNotifier editEventsRealtimeNotifier;
    private final IEditsSubscriptionManager editsSubscriptionManager;

    @Autowired
    public AddEditCommandHandler(
            IUserRepository userRepository,
            IWikiRepository wikiRepository,
            IEditRepository editRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier,
            IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier,
            IEditEventsRealtimeNotifier editEventsRealtimeNotifier,
            IEditsSubscriptionManager editsSubscriptionManager) {
        this.userRepository = userRepository;
        this.wikiRepository = wikiRepository;
        this.editRepository = editRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
        this.wikiEventsRealtimeNotifier = wikiEventsRealtimeNotifier;
        this.editEventsRealtimeNotifier = editEventsRealtimeNotifier;
        this.editsSubscriptionManager = editsSubscriptionManager;
    }

    @Override
    public Mono<Edit> execute(AddEditCommand command) {
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
                .flatMap(userWikiTuple -> this.createEdit(
                        userWikiTuple,
                        command.id(),
                        command.timestamp(),
                        command.title(),
                        command.comment()
                ));
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

    private Mono<Edit> createEdit(
            Tuple2<User, Wiki> userWikiTuple,
            String id,
            LocalDateTime timestamp,
            String title,
            String comment
    ) {
        var user = userWikiTuple.getT1();
        var wiki = userWikiTuple.getT2();
        return Mono
                .just(new Edit(id, timestamp, title, comment, user, wiki))
                .delayUntil(editRepository::add)
                .delayUntil(editEventsRealtimeNotifier::notifyEditCreated)
                .delayUntil(this::notifySubscribedUserEditCreated);
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

    private Mono<Edit> notifySubscribedUserEditCreated(Edit edit) {
        return Mono.just(edit)
                .filter(e -> editsSubscriptionManager.isSubscribedForUserEdits(e.getEditor().getId()))
                .delayUntil(editEventsRealtimeNotifier::notifySubscribedUserEditCreated);
    }
}
