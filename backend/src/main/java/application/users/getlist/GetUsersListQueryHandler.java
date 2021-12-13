package application.users.getlist;

import application.contracts.IQueryHandler;
import reactor.core.publisher.Flux;

public class GetUsersListQueryHandler implements IQueryHandler<GetUsersListQuery, UserDto> {
    @Override
    public Flux<UserDto> execute(GetUsersListQuery query) {
        UserDto[] users = { //TODO: replace by call to the repository
            new UserDto("1","First user"),
            new UserDto("2","Second user")
        };

        return Flux.fromArray(users)
                .skip((long) query.page() * query.count())
                .take(query.count());
    }
}
