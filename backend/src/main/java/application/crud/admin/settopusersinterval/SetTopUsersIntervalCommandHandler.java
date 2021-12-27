package application.crud.admin.settopusersinterval;

import application.crud.admin.session.ISessionRepository;
import application.crud.contracts.ICommandHandler;
import application.crud.users.topusers.TopUsersInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SetTopUsersIntervalCommandHandler implements ICommandHandler<SetTopUsersIntervalCommand, Void> {
    private final ISessionRepository sessionRepository;

    @Autowired
    public SetTopUsersIntervalCommandHandler(
            ISessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Mono<Void> execute(SetTopUsersIntervalCommand command) {
        if (command.interval().equals("day")) {
            sessionRepository.setTopUsersInterval(TopUsersInterval.DAY);
        }

        if (command.interval().equals("month")) {
            sessionRepository.setTopUsersInterval(TopUsersInterval.MONTH);
        }

        if (command.interval().equals("year")) {
            sessionRepository.setTopUsersInterval(TopUsersInterval.YEAR);
        }

        return Mono.empty();
    }
}