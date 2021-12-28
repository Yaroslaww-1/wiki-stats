package application.crud.users.getstats;

import application.crud.contracts.IQueryHandler;
import domain.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetUsersStatsQueryHandler implements IQueryHandler<GetUsersStatsQuery, UsersStatsDto> {
    private final IUserRepository userRepository;

    @Autowired
    public GetUsersStatsQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UsersStatsDto> execute(GetUsersStatsQuery query) {
        return userRepository.getTotalCount()
                .map(UsersStatsDto::new);
    }
}
