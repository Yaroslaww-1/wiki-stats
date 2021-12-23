package infrastructure.postgres.wikis;

import application.wikis.IWikiRepository;
import domain.wiki.Wiki;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    public Mono<Wiki> getByName(String name) {
        return connection.template.select(WikiEntity.class)
                .matching(
                        query(where("name").is(name))
                )
                .one()
                .map(this::mapEntityToDomain);
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
