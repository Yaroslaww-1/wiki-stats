package application.users.getstats;

import application.contracts.IQueryHandler;
import application.users.IUserRepository;
import org.reactivestreams.Publisher;
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
