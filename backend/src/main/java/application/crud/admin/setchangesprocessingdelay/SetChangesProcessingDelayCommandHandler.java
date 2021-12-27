package application.crud.admin.setchangesprocessingdelay;

import application.crud.admin.session.ISessionRepository;
import application.crud.contracts.ICommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SetChangesProcessingDelayCommandHandler implements ICommandHandler<SetChangesProcessingDelayCommand, Void> {
    private final ISessionRepository sessionRepository;

    @Autowired
    public SetChangesProcessingDelayCommandHandler(
            ISessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Mono<Void> execute(SetChangesProcessingDelayCommand query) {
        sessionRepository.setChangesProcessingDelay(Duration.ofMillis(query.delay()));
        return Mono.empty();
    }
}