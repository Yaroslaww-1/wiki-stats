package infrastructure.postgres.users;

import application.crud.users.IUserRepository;
import domain.user.User;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    private User mapEntityToDomain(UserEntity userEntity) {
        return new User(userEntity.id(), userEntity.name(), userEntity.isBot());
    }

    private UserEntity mapDomainToEntity(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getIsBot());
    }
}