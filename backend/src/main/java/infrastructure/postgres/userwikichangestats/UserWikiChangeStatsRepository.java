package infrastructure.postgres.userwikichangestats;

import application.changes.IUserWikiChangeStatsRepository;
import domain.user.UserWikiChangeStats;
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
public class UserWikiChangeStatsRepository implements IUserWikiChangeStatsRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserWikiChangeStatsRepository(
            PostgresConnectionFactory connection
    ) {
        this.connection = connection;
    }

    @Override
    public Mono<UserWikiChangeStats> getOne(Query query) {
        return connection.template.select(UserWikiChangeStatsEntity.class)
                .matching(query.sort(Sort.by("changes_count").descending()))
                .first()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<UserWikiChangeStats> getAll(Query query) {
        return connection.template.select(UserWikiChangeStatsEntity.class)
                .matching(query.sort(Sort.by("changes_count").descending()))
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserWikiChangeStats> add(UserWikiChangeStats userWikiChangeStats) {
        return connection.template.insert(UserWikiChangeStatsEntity.class)
                .using(this.mapDomainToEntity(userWikiChangeStats))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserWikiChangeStats> update(UserWikiChangeStats userWikiChangeStats) {
        return connection.template.update(UserWikiChangeStatsEntity.class)
                .matching(query(where("id").is(userWikiChangeStats.getId())))
                .apply(
                        Update
                                .update("changes_count", userWikiChangeStats.getChangesCount())
                                .set("user_id", userWikiChangeStats.getUserId())
                                .set("wiki_id", userWikiChangeStats.getWikiId())
                )
                .thenReturn(userWikiChangeStats);
    }

    private UserWikiChangeStats mapEntityToDomain(UserWikiChangeStatsEntity userWikiChangeStatsEntity) {
        return new UserWikiChangeStats(
                userWikiChangeStatsEntity.id(),
                userWikiChangeStatsEntity.changesCount(),
                userWikiChangeStatsEntity.userId(),
                userWikiChangeStatsEntity.wikiId()
        );
    }

    private UserWikiChangeStatsEntity mapDomainToEntity(UserWikiChangeStats userWikiChangeStats) {
        return new UserWikiChangeStatsEntity(
                userWikiChangeStats.getId(),
                userWikiChangeStats.getChangesCount(),
                userWikiChangeStats.getUserId(),
                userWikiChangeStats.getWikiId()
        );
    }
}
