package infrastructure.postgres.edits;

import domain.edit.Edit;
import domain.edit.IEditRepository;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EditRepository implements IEditRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public EditRepository(PostgresConnectionFactory connection) {
        this.connection = connection;
    }

    @Override
    public Mono<Edit> add(Edit edit) {
        return connection.template.insert(EditEntity.class)
                .using(this.mapDomainToEntity(edit))
                .thenReturn(edit);
    }

    private EditEntity mapDomainToEntity(Edit edit) {
        return new EditEntity(
                edit.getId(),
                edit.getTimestamp(),
                edit.getTitle(),
                edit.getComment(),
                edit.getEditor().getId(),
                edit.getWiki().getId()
        );
    }
}
