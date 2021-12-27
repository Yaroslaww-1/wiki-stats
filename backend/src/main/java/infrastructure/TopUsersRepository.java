package infrastructure;

import application.users.topusers.ITopUsersRepository;
import application.users.topusers.TopUsers;
import application.users.topusers.TopUsersInterval;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
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
    public Flux<TopUsers> insertAndReturnOrderedByInterval(String userName, Long changesCount, TopUsersInterval interval) {
        insertAndReturnOrdered(userName, changesCount, this.topDayUsers);
        insertAndReturnOrdered(userName, changesCount, this.topMonthUsers);
        insertAndReturnOrdered(userName, changesCount, this.topYearUsers);

        if (userName.equals("Kappa")) {
            System.out.println(changesCount);
        }

        if (interval == TopUsersInterval.DAY) {
            return Flux.fromStream(this.topDayUsers.stream().limit(10));
        }

        if (interval == TopUsersInterval.MONTH) {
            return Flux.fromStream(this.topMonthUsers.stream().limit(10));
        }

        if (interval == TopUsersInterval.YEAR) {
            return Flux.fromStream(this.topYearUsers.stream().limit(0)); //TODO: remove (for testing only)
        }

        return Flux.empty();
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
        if (topUsers.size() >= 10) {
            topUsers.remove(topUsers.size() - 1);
        }
    }

//    @Override
//    public Mono<Void> set(List<TopUsers> topUsers, TopUsersInterval interval) {
//        if (interval == TopUsersInterval.DAY) {
//            this.topDayUsers.clear();
//            this.topDayUsers.addAll(topUsers);
//        }
//
//        if (interval == TopUsersInterval.MONTH) {
//            this.topMonthUsers.clear();
//            this.topMonthUsers.addAll(topUsers);
//        }
//
//        if (interval == TopUsersInterval.YEAR) {
//            this.topYearUsers.clear();
//            this.topYearUsers.addAll(topUsers);
//        }
//
//        return Mono.empty();
//    }
}
