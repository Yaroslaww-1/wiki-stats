package application.users.getusereditsstats;

import application.contracts.IQueryHandler;
import application.users.IUserEditStatsEntityRepository;
import application.users.IUserEventsRealtimeNotifier;
import application.users.IUserRepository;
import domain.user.User;
import domain.user.UserEditStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class GetUserEditsStatsQueryHandler implements IQueryHandler<GetUserEditsStatsQuery, UserEditsStatsDto> {
    private final IUserRepository userRepository;
    private final IUserEventsRealtimeNotifier userEventsRealtimeNotifier;
    private final IUserEditStatsEntityRepository userEditStatsEntityRepository;

    public GetUserEditsStatsQueryHandler(
            IUserRepository userRepository,
            IUserEventsRealtimeNotifier userEventsRealtimeNotifier,
            IUserEditStatsEntityRepository userEditStatsEntityRepository
    ) {
        this.userRepository = userRepository;
        this.userEventsRealtimeNotifier = userEventsRealtimeNotifier;
        this.userEditStatsEntityRepository = userEditStatsEntityRepository;
    }

    @Override
    public Mono<UserEditsStatsDto> execute(GetUserEditsStatsQuery query) {
        var userMono = userRepository.getOne(
                        query(where("name").is(query.userName()))
                )
                .switchIfEmpty(this.createUser(query.userName()));

        return userMono
                .flatMap(user -> userEditStatsEntityRepository
                        .getAll(
                                query(where("user_id").is(user.getId()))
                                        .limit(query.windowDurationInMinutes().intValue())
                        )
                        .collectList()
                        .map(existingEditStats -> this.getWindowedEditsStats(
                                existingEditStats,
                                query.stepInMinutes(),
                                query.windowDurationInMinutes()
                        ))
                )
                .map(UserEditsStatsDto::new);

    }

    private Mono<User> createUser(String editor) {
        return Mono
                .just(new User(editor, false))
                .delayUntil(userRepository::add)
                .delayUntil(userEventsRealtimeNotifier::notifyUserCreated);
    }

    private List<UserEditsStatsPart> getWindowedEditsStats(
            List<UserEditStats> existingEditStats,
            Long stepInMinutes,
            Long windowInMinutes
    ) {
        var windowStart = LocalDateTime.now().minusMinutes(windowInMinutes);
        var windowedEditsStats = new ArrayList<UserEditsStatsPart>();

        while (windowStart.isBefore(LocalDateTime.now())) {
            var editsCountInWindow = 0L;
            var windowEnd = windowStart.plusMinutes(stepInMinutes);

            for (var editStats : existingEditStats) {
                var editStatsDate = editStats.getStartTimestamp();
                if (
                    (editStatsDate.isAfter(windowStart) && editStatsDate.isBefore(windowEnd)) ||
                    editStatsDate.equals(windowStart) ||
                    editStatsDate.equals(windowEnd)
                ) {
                    editsCountInWindow += editStats.getEditCount();
                }
            }

            windowedEditsStats.add(new UserEditsStatsPart(
                    (long)windowedEditsStats.size(),
                    editsCountInWindow,
                    stepInMinutes,
                    windowEnd.toString())
            );

            windowStart = windowStart.plusMinutes(stepInMinutes);
        }

        return windowedEditsStats;
    }
}
