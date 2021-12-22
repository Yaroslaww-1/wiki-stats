package application.edits.addedit;

import application.contracts.ICommandHandler;
import domain.edit.Edit;
import domain.edit.IEditRepository;
import domain.user.IUserRepository;
import domain.user.User;
import domain.wiki.IWikiRepository;
import domain.wiki.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AddEditCommandHandler implements ICommandHandler<AddEditCommand, Edit> {
    private final IUserRepository userRepository;
    private final IWikiRepository wikiRepository;
    private final IEditRepository editRepository;

    @Autowired
    public AddEditCommandHandler(IUserRepository userRepository, IWikiRepository wikiRepository, IEditRepository editRepository) {
        this.userRepository = userRepository;
        this.wikiRepository = wikiRepository;
        this.editRepository = editRepository;
    }

    @Override
    public Mono<Edit> execute(AddEditCommand command) {
        var userMono = userRepository.getByName(command.editor())
                .switchIfEmpty(userRepository.add(new User(command.editor(), command.isBot())));

        var wikiMono = wikiRepository.getByName(command.wiki())
                .switchIfEmpty(wikiRepository.add(new Wiki(command.wiki())));

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
                })
                .log();
    }
}
