package application.edits.addedit;

import application.contracts.ICommandHandler;
import application.users.IUserEventsRealtimeNotifier;
import domain.edit.Edit;
import application.edits.IEditRepository;
import application.users.IUserRepository;
import domain.user.User;
import application.wikis.IWikiRepository;
import domain.wiki.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AddEditCommandHandler implements ICommandHandler<AddEditCommand, Edit> {
    private final IUserRepository userRepository;
    private final IWikiRepository wikiRepository;
    private final IEditRepository editRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;

    @Autowired
    public AddEditCommandHandler(
            IUserRepository userRepository,
            IWikiRepository wikiRepository,
            IEditRepository editRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier
    ) {
        this.userRepository = userRepository;
        this.wikiRepository = wikiRepository;
        this.editRepository = editRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
    }

    @Override
    public Mono<Edit> execute(AddEditCommand command) {
        var userMono = userRepository.getByName(command.editor())
                .switchIfEmpty(this.createUser(command.editor(), command.isBot()));

        var wikiMono = wikiRepository.getByName(command.wiki())
                .switchIfEmpty(this.createWiki(command.wiki()));

        return Mono.zip(userMono, wikiMono)
                .flatMap((userWikiTuple) -> {
                    var user = userWikiTuple.getT1();
                    var wiki = userWikiTuple.getT2();
                    var edit = new Edit(
                            command.id(),
                            command.timestamp(),
                            command.title(),
                            command.comment(),
                            user,
                            wiki
                    );
                    return editRepository.add(edit);
                });
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
                .delayUntil(wikiRepository::add);
    }
}
