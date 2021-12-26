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
                                        .limit(365)
                        )
                        .collectList()
                        .map(existingEditStats -> this.getWindowedEditsStats(existingEditStats, query.windowDurationInDays()))
                )
                .map(UserEditsStatsDto::new);

    }

    private Mono<User> createUser(String editor) {
        return Mono
                .just(new User(editor, false))
                .delayUntil(userRepository::add)
                .delayUntil(userEventsRealtimeNotifier::notifyUserCreated);
    }

    private List<UserEditsStatsPart> getWindowedEditsStats(List<UserEditStats> existingEditStats, Long windowDurationInDays) {
        var windowStart = LocalDateTime.now().minusYears(1);
        var windowedEditsStats = new ArrayList<UserEditsStatsPart>();

        while (windowStart.isBefore(LocalDateTime.now())) {
            var editsCountInWindow = 0L;
            var windowEnd = windowStart.plusDays(windowDurationInDays);

            for (var editStats : existingEditStats) {
                var editStatsDate = LocalDateTime.of(0, 1, 1, 0, 0)
                        .plusYears(editStats.getYear())
                        .plusDays(editStats.getDay());
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
                    windowDurationInDays,
                    windowEnd.getYear(),
                    windowEnd.getDayOfYear())
            );

            windowStart = windowStart.plusDays(windowDurationInDays);
        }

        return windowedEditsStats;
    }

//    private List<UserEditsStatsPart> getWindowedEditsStats(List<UserEditStats> existingEditStats, Long windowDurationInDays) {
//        var windowStart = LocalDateTime.now().minusYears(1);
//        var windowedEditsStats = new ArrayList<UserEditsStatsPart>();
//        var index = 0L;
//
//
//        while (windowStart.isBefore(LocalDateTime.now())) {
////            var existingDataInWindow = new ArrayList<UserEditStats>();
//            var editsCountInWindow = 0L;
//            var windowEnd = windowStart.plusDays(windowDurationInDays);
//
//            for (var editStats : existingEditStats) {
//                var editStatsDate = LocalDateTime.of(0, 0, 0, 0, 0)
//                        .plusYears(editStats.getYear())
//                        .plusDays(editStats.getDay());
////                var isYearInWindow = (windowStart.getYear() <= data.getYear() && data.getYear() <= windowEnd.getYear());
////                var isDayInWindow = (
////                        windowStart.getDayOfYear() <= data.getDay() &&
////                        data.getDay() <= windowEnd.getDayOfYear() &&
////
////                        );
////                if (data.getYear())
//                if (
//                        (editStatsDate.isAfter(windowStart) && editStatsDate.isBefore(windowEnd)) ||
//                                editStatsDate.equals(windowStart) ||
//                                editStatsDate.equals(windowEnd)
//                ) {
//                    editsCountInWindow += editStats.getEditCount();
////                    existingDataInWindow.add(editStats);
//                }
//            }
//
//            windowedEditsStats.add(new UserEditsStatsPart(
//                    index,
//                    editsCountInWindow,
//                    windowDurationInDays,
//                    windowEnd.getYear(),
//                    windowEnd.getDayOfYear())
//            );
//
////            if (existingDataInWindow.isEmpty()) {
////                windowedEditsStats.add(new UserEditsStatsPart(
////                        index,
////                        0L,
////                        windowDurationInDays,
////                        windowEnd.getYear(),
////                        windowEnd.getDayOfYear())
////                );
////            } else {
////                var edi
////                windowedEditsStats.add(new UserEditsStatsPart(
////                        index,
////                        0L,
////                        windowDurationInDays,
////                        windowEnd.getYear(),
////                        windowEnd.getDayOfYear())
////                );
////            }
//
//            windowStart = windowStart.plusDays(windowDurationInDays);
//            index++;
//        }
//
//        return windowedEditsStats;
//
////        for (var a : existingData) {
////            while (currentCheckDate.getDayOfYear() != a.getDay() && currentCheckDate.getYear() != a.getYear()) {
////                editsStatsWithMissingData.add(new UserEditsStatsPart(
////                      index, 0L, windowDurationInDays, true
////                ))
////                currentCheckDate = currentCheckDate.plusDays(1);
////            }
////        }
////
////        return editsStatsWithMissingData;
//    }
}
