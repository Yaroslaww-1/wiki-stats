package infrastructure.postgres.userchangesinterval;

import domain.userchangesinterval.IUserChangesIntervalRepository;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;


@Component
public class UserChangesIntervalRepository implements IUserChangesIntervalRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserChangesIntervalRepository(
            PostgresConnectionFactory connection
    ) {
        this.connection = connection;
    }

    @Override
    public Mono<domain.userchangesinterval.UserChangesInterval> getOne(Query query) {
        return connection.template.select(UserChangesIntervalEntity.class)
                .matching(query.sort(Sort.by("start_timestamp").descending()))
                .first()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<domain.userchangesinterval.UserChangesInterval> getAll(Query query) {
        return connection.template.select(UserChangesIntervalEntity.class)
                .matching(query.sort(Sort.by("start_timestamp").descending()))
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<domain.userchangesinterval.UserChangesInterval> add(domain.userchangesinterval.UserChangesInterval userChangesInterval) {
        return connection.template.insert(UserChangesIntervalEntity.class)
                .using(this.mapDomainToEntity(userChangesInterval))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<domain.userchangesinterval.UserChangesInterval> update(domain.userchangesinterval.UserChangesInterval userChangesInterval) {
        return connection.template.update(UserChangesIntervalEntity.class)
                .matching(query(where("id").is(userChangesInterval.getId())))
                .apply(
                        Update
                                .update("start_timestamp", userChangesInterval.getStartTimestamp())
                                .set("duration_in_minutes", userChangesInterval.getDurationInMinutes())
                                .set("adds_count", userChangesInterval.getAddsCount())
                                .set("edits_count", userChangesInterval.getEditsCount())
                                .set("user_id", userChangesInterval.getUserId())
                )
                .thenReturn(userChangesInterval);
    }

    private domain.userchangesinterval.UserChangesInterval mapEntityToDomain(UserChangesIntervalEntity userChangesIntervalEntity) {
        return new domain.userchangesinterval.UserChangesInterval(
                userChangesIntervalEntity.id(),
                userChangesIntervalEntity.startTimestamp(),
                userChangesIntervalEntity.durationInMinutes(),
                userChangesIntervalEntity.addsCount(),
                userChangesIntervalEntity.editsCount(),
                userChangesIntervalEntity.userId()
        );
    }

    private UserChangesIntervalEntity mapDomainToEntity(domain.userchangesinterval.UserChangesInterval userChangesInterval) {
        return new UserChangesIntervalEntity(
                userChangesInterval.getId(),
                userChangesInterval.getStartTimestamp(),
                userChangesInterval.getDurationInMinutes(),
                userChangesInterval.getAddsCount(),
                userChangesInterval.getEditsCount(),
                userChangesInterval.getUserId()
        );
    }
}
