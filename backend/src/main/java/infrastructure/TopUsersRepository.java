package infrastructure;

import application.crud.users.topusers.ITopUsersRepository;
import application.crud.users.topusers.TopUsers;
import application.crud.users.topusers.TopUsersInterval;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class TopUsersRepository implements ITopUsersRepository {
    // TODO: replace by hashmap?
    private final List<TopUsers> topDayUsers = new CopyOnWriteArrayList<TopUsers>();
    private final List<TopUsers> topMonthUsers = new CopyOnWriteArrayList<TopUsers>();
    private final List<TopUsers> topYearUsers = new CopyOnWriteArrayList<TopUsers>();

    @Override
    public Mono<List<TopUsers>> insertAndReturnOrderedByInterval(String userName, Long changesCount, TopUsersInterval interval) {
        insertAndReturnOrdered(userName, changesCount, this.topDayUsers);
        insertAndReturnOrdered(userName, changesCount, this.topMonthUsers);
        insertAndReturnOrdered(userName, changesCount, this.topYearUsers);

        if (interval == TopUsersInterval.DAY) {
            return Mono.just(this.topDayUsers.stream().limit(10).toList());
        }

        if (interval == TopUsersInterval.MONTH) {
            return Mono.just(this.topMonthUsers.stream().limit(10).toList());
        }

        if (interval == TopUsersInterval.YEAR) {
            return Mono.just(this.topYearUsers.stream().limit(10).toList());
        }

        return Mono.empty();
    }

    private void insertAndReturnOrdered(String userName, Long changesCount, List<TopUsers> topUsers) {
        topUsers.removeIf(stats -> stats.userName().equals(userName));

        topUsers.add(new TopUsers(changesCount, userName));
        topUsers.sort(new Comparator<TopUsers>() {
            public int compare(TopUsers o1, TopUsers o2){
                if(o1.changesCount().equals(o2.changesCount()))
                    return 0;
                return o1.changesCount() > o2.changesCount() ? -1 : 1;
            }
        });
        while (topUsers.size() >= 10) {
            topUsers.remove(topUsers.size() - 1);
        }
    }
}
