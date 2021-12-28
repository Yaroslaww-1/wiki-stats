package infrastructure.postgres.userwikis;

import domain.user.User;
import domain.userwiki.IUserWikiRepository;
import domain.userwiki.UserWiki;
import infrastructure.postgres.PostgresConnectionFactory;
import infrastructure.postgres.users.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class UserWikiRepository implements IUserWikiRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserWikiRepository(PostgresConnectionFactory connection) {
        this.connection = connection;
    }

    @Override
    public Mono<UserWiki> getOne(Query query) {
        return connection.template.select(UserWikiEntity.class)
                .matching(query)
                .first()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<UserWiki> getAll(Query query) {
        return connection.template.select(UserWikiEntity.class)
                .matching(query)
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserWiki> add(UserWiki userWiki) {
        return connection.template.insert(UserWikiEntity.class)
                .using(this.mapDomainToEntity(userWiki))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<UserWiki> update(UserWiki userWiki) {
        return connection.template.update(UserWikiEntity.class)
                .matching(query(where("id").is(userWiki.getId())))
                .apply(
                        Update
                                .update("wiki_name", userWiki.getWikiName())
                                .set("changes_count", userWiki.getChangesCount())
                )
                .thenReturn(userWiki);
    }

    private UserWiki mapEntityToDomain(UserWikiEntity entity) {
        return new UserWiki(
                entity.id(),
                entity.changesCount(),
                entity.userId(),
                entity.wikiId(),
                entity.wikiName()
        );
    }

    private UserWikiEntity mapDomainToEntity(UserWiki userWiki) {
        return new UserWikiEntity(
                userWiki.getId(),
                userWiki.getChangesCount(),
                userWiki.getUserId(),
                userWiki.getWikiId(),
                userWiki.getWikiName()
        );
    }
}
