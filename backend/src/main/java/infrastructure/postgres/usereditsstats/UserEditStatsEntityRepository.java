package infrastructure.postgres.usereditsstats;

import application.users.IUserEditStatsEntityRepository;
import domain.user.UserEditStats;
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
public class UserEditStatsEntityRepository implements IUserEditStatsEntityRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserEditStatsEntityRepository(
            PostgresConnectionFactory connection
    ) {
        this.connection = connection;
    }

    @Override
    public Mono<UserEditStats> getOne(Query query) {
        return connection.template.select(UserEditStatsEntity.class)
                .matching(query)
                .one()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<UserEditStats> getAll(Query query) {
        return connection.template.select(UserEditStatsEntity.class)
                .matching(query.sort(Sort.by("year", "day")))
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserEditStats> add(UserEditStats userEditStats) {
        return connection.template.insert(UserEditStatsEntity.class)
                .using(this.mapDomainToEntity(userEditStats))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserEditStats> update(UserEditStats userEditStats) {
        return connection.template.update(UserEditStatsEntity.class)
                .matching(query(where("id").is(userEditStats.getId())))
                .apply(
                        Update
                                .update("day", userEditStats.getDay())
                                .set("year", userEditStats.getYear())
                                .set("add_count", userEditStats.getAddCount())
                                .set("edit_count", userEditStats.getEditCount())
                                .set("user_id", userEditStats.getUserId())
                )
                .thenReturn(userEditStats);
    }

    private UserEditStats mapEntityToDomain(UserEditStatsEntity userEditStatsEntity) {
        return new UserEditStats(
                userEditStatsEntity.id(),
                userEditStatsEntity.day(),
                userEditStatsEntity.year(),
                userEditStatsEntity.addCount(),
                userEditStatsEntity.editCount(),
                userEditStatsEntity.userId()
        );
    }

    private UserEditStatsEntity mapDomainToEntity(UserEditStats userEditStats) {
        return new UserEditStatsEntity(
                userEditStats.getId(),
                userEditStats.getDay(),
                userEditStats.getYear(),
                userEditStats.getAddCount(),
                userEditStats.getEditCount(),
                userEditStats.getUserId()
        );
    }
}
