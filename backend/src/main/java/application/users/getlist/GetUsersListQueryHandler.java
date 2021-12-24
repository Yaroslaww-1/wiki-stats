package application.users.getlist;

import application.contracts.IQueryHandler;
import application.users.IUserRepository;
import domain.user.User;
import infrastructure.postgres.PostgresConnectionFactory;
import infrastructure.postgres.users.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class GetUsersListQueryHandler implements IQueryHandler<GetUsersListQuery, UserDto> {
    private final IUserRepository userRepository;

    @Autowired
    public GetUsersListQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserDto> execute(GetUsersListQuery query) {
        return userRepository.getAll(
                    query(where("name").is(query.name()))
                            .limit(query.count())
                            .offset((long) query.page() * query.count())
                )
                .map(this::mapDomainToDto);
    }

    private UserDto mapDomainToDto(User user) {
        return new UserDto(user.getId(), user.getName());
    }
}
