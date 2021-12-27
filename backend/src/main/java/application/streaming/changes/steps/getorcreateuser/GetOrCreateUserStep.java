package application.streaming.changes.steps.getorcreateuser;

import application.crud.users.IUserChangeAggregateStatsRepository;
import application.crud.users.IUserEventsRealtimeNotifier;
import application.crud.users.IUserRepository;
import application.streaming.contracts.IStep;
import domain.user.User;
import domain.user.UserChangeAggregateStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class GetOrCreateUserStep implements IStep<GetOrCreateUserStepInput, User> {
    private final IUserRepository userRepository;
    private final IUserChangeAggregateStatsRepository userChangeAggregateStatsRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;

    public GetOrCreateUserStep(
            IUserRepository userRepository,
            IUserChangeAggregateStatsRepository userChangeAggregateStatsRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier
    ) {
        this.userRepository = userRepository;
        this.userChangeAggregateStatsRepository = userChangeAggregateStatsRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
    }

    @Override
    public Mono<User> execute(GetOrCreateUserStepInput input) {
        var userName = getUserName(input.userName());

        return userRepository
                    .getOne(query(where("name").is(userName)))
                    .switchIfEmpty(this.createUser(userName, input.isBot()));
    }

    /**
     * With 10% chance replaces initial editor name by hardcoded one, so we can test further functionality more easily
     * @param inputUserName
     * @return Username from command of hardcoded editor name
     */
    private String getUserName(String inputUserName) {
        Random rand = new Random();
        if (rand.nextInt(100) <= 10) {
            return "Kappa";
        } else {
            return inputUserName;
        }
    }

    private Mono<User> createUser(String editor, Boolean isBot) {
        return Mono
                .just(new User(editor, isBot))
                .delayUntil(userRepository::add)
                .delayUntil(user -> userChangeAggregateStatsRepository.add(
                        new UserChangeAggregateStats(user.getId())
                ))
                .delayUntil(userEventsRealtimeNotifier::notifyUserCreated);
    }
}
