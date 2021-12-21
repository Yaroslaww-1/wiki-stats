package infrastructure.postgres.users;

import domain.user.IUserRepository;
import domain.user.User;
import infrastructure.postgres.PostgresConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
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
    public Mono<User> getByName(String name) {
        return connection.template.select(UserEntity.class)
                .matching(
                        query(where("name").is(name))
                )
                .one()
                .map(this::mapEntityToDomain);
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