package infrastructure.postgres.userchangeaggregatestats;

import application.crud.users.IUserChangeAggregateStatsRepository;
import domain.user.UserChangeAggregateStats;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class UserChangeAggregateStatsRepository implements IUserChangeAggregateStatsRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserChangeAggregateStatsRepository(
            PostgresConnectionFactory connection
    ) {
        this.connection = connection;
    }

    @Override
    public Mono<UserChangeAggregateStats> getOne(Query query) {
        return connection.template.select(UserChangeAggregateStatsEntity.class)
                .matching(query)
                .first()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<UserChangeAggregateStats> getAll(Query query) {
        return connection.template.select(UserChangeAggregateStatsEntity.class)
                .matching(query)
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserChangeAggregateStats> add(UserChangeAggregateStats userChangeAggregateStats) {
        return connection.template.insert(UserChangeAggregateStatsEntity.class)
                .using(this.mapDomainToEntity(userChangeAggregateStats))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserChangeAggregateStats> update(UserChangeAggregateStats userChangeAggregateStats) {
        return connection.template.update(UserChangeAggregateStatsEntity.class)
                .matching(query(where("id").is(userChangeAggregateStats.getId())))
                .apply(
                        Update
                                .update("user_id", userChangeAggregateStats.getUserId())
                                .set("add_count", userChangeAggregateStats.getAddCount())
                                .set("edit_count", userChangeAggregateStats.getEditCount())
                )
                .thenReturn(userChangeAggregateStats);
    }

    private UserChangeAggregateStats mapEntityToDomain(UserChangeAggregateStatsEntity userChangeAggregateStatsEntity) {
        return new UserChangeAggregateStats(
                userChangeAggregateStatsEntity.id(),
                userChangeAggregateStatsEntity.userId(),
                userChangeAggregateStatsEntity.addCount(),
                userChangeAggregateStatsEntity.editCount()
        );
    }

    private UserChangeAggregateStatsEntity mapDomainToEntity(UserChangeAggregateStats userChangeAggregateStats) {
        return new UserChangeAggregateStatsEntity(
                userChangeAggregateStats.getId(),
                userChangeAggregateStats.getUserId(),
                userChangeAggregateStats.getAddCount(),
                userChangeAggregateStats.getEditCount()
        );
    }
}
