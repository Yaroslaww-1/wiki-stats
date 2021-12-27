package application.users.getuserchangesstats;

import application.contracts.IQueryHandler;
import application.users.IUserChangeStatsRepository;
import application.users.IUserEventsRealtimeNotifier;
import application.users.IUserRepository;
import domain.user.User;
import domain.user.UserChangeStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class GetUserChangesStatsQueryHandler implements IQueryHandler<GetUserChangesStatsQuery, UserChangesStatsDto> {
    private final IUserRepository userRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;
    private final IUserChangeStatsRepository userChangeStatsRepository;

    public GetUserChangesStatsQueryHandler(
            IUserRepository userRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier,
            IUserChangeStatsRepository userChangeStatsRepository
    ) {
        this.userRepository = userRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
        this.userChangeStatsRepository = userChangeStatsRepository;
    }

    @Override
    public Mono<UserChangesStatsDto> execute(GetUserChangesStatsQuery query) {
        var userMono = userRepository.getOne(
                        query(where("name").is(query.userName()))
                )
                .switchIfEmpty(this.createUser(query.userName()));

        return userMono
                .flatMap(user -> userChangeStatsRepository
                        .getAll(
                                query(where("user_id").is(user.getId()))
                                        .limit(query.windowDurationInMinutes().intValue())
                        )
                        .collectList()
                        .map(existingChangeStats -> this.getWindowedChangeStats(
                                existingChangeStats,
                                query.stepInMinutes(),
                                query.windowDurationInMinutes()
                        ))
                )
                .map(UserChangesStatsDto::new);

    }

    private Mono<User> createUser(String editor) {
        return Mono
                .just(new User(editor, false))
                .delayUntil(userRepository::add)
                .delayUntil(userEventsRealtimeNotifier::notifyUserCreated);
    }

    private List<UserChangesStatsPart> getWindowedChangeStats(
            List<UserChangeStats> existingChangeStats,
            Long stepInMinutes,
            Long windowInMinutes
    ) {
        var windowStart = LocalDateTime.now().minusMinutes(windowInMinutes);
        var windowedChangesStats = new ArrayList<UserChangesStatsPart>();

        while (windowStart.isBefore(LocalDateTime.now())) {
            var changesCountInWindow = 0L;
            var windowEnd = windowStart.plusMinutes(stepInMinutes);

            for (var changeStats : existingChangeStats) {
                var changeStatsDate = changeStats.getStartTimestamp();
                if (
                    (changeStatsDate.isAfter(windowStart) && changeStatsDate.isBefore(windowEnd)) ||
                    changeStatsDate.equals(windowStart) ||
                    changeStatsDate.equals(windowEnd)
                ) {
                    changesCountInWindow += changeStats.getChangesCount();
                }
            }

            windowedChangesStats.add(new UserChangesStatsPart(
                    (long)windowedChangesStats.size(),
                    changesCountInWindow,
                    stepInMinutes,
                    windowEnd.toString())
            );

            windowStart = windowStart.plusMinutes(stepInMinutes);
        }

        return windowedChangesStats;
    }
}
