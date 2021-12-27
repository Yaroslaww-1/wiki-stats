package application.admin.resetsession;

import application.admin.session.ISessionRepository;
import application.contracts.ICommandHandler;
import application.users.topusers.TopUsersInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ResetSessionCommandHandler implements ICommandHandler<ResetSessionCommand, Void> {
    private final ISessionRepository sessionRepository;

    @Autowired
    public ResetSessionCommandHandler(
            ISessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Mono<Void> execute(ResetSessionCommand command) {
        sessionRepository.setChangesProcessingDelay(Duration.ZERO);
        sessionRepository.unsubscribeAll();
        sessionRepository.setTopUsersInterval(TopUsersInterval.YEAR);
        return Mono.empty();
    }
}