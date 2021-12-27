package infrastructure.postgres.changes;

import domain.change.Change;
import application.streaming.changes.steps.createchange.IChangeRepository;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ChangeRepository implements IChangeRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public ChangeRepository(PostgresConnectionFactory connection) {
        this.connection = connection;
    }

    @Override
    public Mono<Change> add(Change change) {
        return connection.template.insert(ChangeEntity.class)
                .using(this.mapDomainToEntity(change))
                .thenReturn(change);
    }

    private ChangeEntity mapDomainToEntity(Change change) {
        return new ChangeEntity(
                change.getId(),
                change.getTimestamp(),
                change.getTitle(),
                change.getComment(),
                change.getType(),
                change.getEditor().getId(),
                change.getWiki().getId()
        );
    }
}
