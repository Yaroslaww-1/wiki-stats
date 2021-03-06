package infrastructure.postgres.wikis;

import domain.wiki.IWikiRepository;
import domain.wiki.Wiki;
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
public class WikiRepository implements IWikiRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public WikiRepository(PostgresConnectionFactory connection) {
        this.connection = connection;
    }

    @Override
    public Mono<Wiki> getOne(Query query) {
        return connection.template.select(WikiEntity.class)
                .matching(query)
                .one()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<Wiki> getAll(Query query) {
        return connection.template.select(WikiEntity.class)
                .matching(query)
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<Long> getTotalCount() {
        return connection.template.count(
                query(Criteria.empty()),
                WikiEntity.class
        );
    }

    @Override
    public Mono<Wiki> add(Wiki wiki) {
        return connection.template.insert(WikiEntity.class)
                .using(this.mapDomainToEntity(wiki))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<Wiki> update(Wiki wiki) {
        return connection.template.update(WikiEntity.class)
                .matching(query(where("id").is(wiki.getId())))
                .apply(
                        Update
                                .update("name", wiki.getName())
                                .set("edits_count", wiki.getEditsCount())
                )
                .thenReturn(wiki);
    }

    private Wiki mapEntityToDomain(WikiEntity wikiEntity) {
        return new Wiki(
                wikiEntity.id(),
                wikiEntity.name(),
                wikiEntity.editsCount()
        );
    }

    private WikiEntity mapDomainToEntity(Wiki wiki) {
        return new WikiEntity(
                wiki.getId(),
                wiki.getName(),
                wiki.getEditsCount()
        );
    }
}
