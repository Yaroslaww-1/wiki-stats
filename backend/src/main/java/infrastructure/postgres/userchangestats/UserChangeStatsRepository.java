package infrastructure.postgres.userchangestats;

import application.users.IUserChangeStatsRepository;
import domain.user.UserChangeStats;
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
public class UserChangeStatsRepository implements IUserChangeStatsRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserChangeStatsRepository(
            PostgresConnectionFactory connection
    ) {
        this.connection = connection;
    }

    @Override
    public Mono<UserChangeStats> getOne(Query query) {
        return connection.template.select(UserChangeStatsEntity.class)
                .matching(query.sort(Sort.by("start_timestamp").descending()))
                .first()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<UserChangeStats> getAll(Query query) {
        return connection.template.select(UserChangeStatsEntity.class)
                .matching(query.sort(Sort.by("start_timestamp").descending()))
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserChangeStats> add(UserChangeStats userChangeStats) {
        return connection.template.insert(UserChangeStatsEntity.class)
                .using(this.mapDomainToEntity(userChangeStats))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserChangeStats> update(UserChangeStats userChangeStats) {
        return connection.template.update(UserChangeStatsEntity.class)
                .matching(query(where("id").is(userChangeStats.getId())))
                .apply(
                        Update
                                .update("start_timestamp", userChangeStats.getStartTimestamp())
                                .set("duration_in_minutes", userChangeStats.getDurationInMinutes())
                                .set("add_count", userChangeStats.getAddCount())
                                .set("edit_count", userChangeStats.getEditCount())
                                .set("user_id", userChangeStats.getUserId())
                )
                .thenReturn(userChangeStats);
    }

    private UserChangeStats mapEntityToDomain(UserChangeStatsEntity userChangeStatsEntity) {
        return new UserChangeStats(
                userChangeStatsEntity.id(),
                userChangeStatsEntity.startTimestamp(),
                userChangeStatsEntity.durationInMinutes(),
                userChangeStatsEntity.addCount(),
                userChangeStatsEntity.editCount(),
                userChangeStatsEntity.userId()
        );
    }

    private UserChangeStatsEntity mapDomainToEntity(UserChangeStats userChangeStats) {
        return new UserChangeStatsEntity(
                userChangeStats.getId(),
                userChangeStats.getStartTimestamp(),
                userChangeStats.getDurationInMinutes(),
                userChangeStats.getAddCount(),
                userChangeStats.getEditCount(),
                userChangeStats.getUserId()
        );
    }
}
