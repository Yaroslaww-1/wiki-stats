package infrastructure.postgres.wikis;

import application.crud.wikis.IWikiRepository;
import domain.wiki.Wiki;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    private Wiki mapEntityToDomain(WikiEntity wikiEntity) {
        return new Wiki(wikiEntity.id(), wikiEntity.name());
    }

    private WikiEntity mapDomainToEntity(Wiki wiki) {
        return new WikiEntity(wiki.getId(), wiki.getName());
    }
}
