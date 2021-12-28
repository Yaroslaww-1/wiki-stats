package infrastructure.postgres.users;

import domain.user.IUserRepository;
import domain.user.User;
import infrastructure.postgres.PostgresConnectionFactory;
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
class UserRepository implements IUserRepository {
    private final PostgresConnectionFactory connection;

    @Autowired
    public UserRepository(PostgresConnectionFactory connection) {
        this.connection = connection;
    }

    @Override
    public Mono<User> getOne(Query query) {
        return connection.template.select(UserEntity.class)
                .matching(query)
                .first()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Flux<User> getAll(Query query) {
        return connection.template.select(UserEntity.class)
                .matching(query)
                .all()
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<Long> getTotalCount() {
        return connection.template.count(
                query(Criteria.empty()),
                UserEntity.class
        );
    }

    @Override
    public Mono<User> add(User user) {
        return connection.template.insert(UserEntity.class)
                .using(this.mapDomainToEntity(user))
                .map(this::mapEntityToDomain);
    }

    @Override
    public Mono<User> update(User user) {
        return connection.template.update(UserEntity.class)
                .matching(query(where("id").is(user.getId())))
                .apply(
                        Update
                                .update("name", user.getName())
                                .set("is_bot", user.getIsBot())
                                .set("adds_count", user.getAddsCount())
                                .set("edits_count", user.getEditsCount())
                )
                .thenReturn(user);
    }

    private User mapEntityToDomain(UserEntity userEntity) {
        return new User(
                userEntity.id(),
                userEntity.name(),
                userEntity.isBot(),
                userEntity.addsCount(),
                userEntity.editsCount()
        );
    }

    private UserEntity mapDomainToEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getIsBot(),
                user.getAddsCount(),
                user.getEditsCount()
        );
    }
}