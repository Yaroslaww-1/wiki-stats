package infrastructure.inmemory;

import domain.user.ITopUserRepository;
import domain.user.TopUsersInterval;
import domain.user.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class TopUserRepository implements ITopUserRepository {
    // TODO: replace by hashmap?
    private final List<User> topDayUsers = new CopyOnWriteArrayList<User>();
    private final List<User> topMonthUsers = new CopyOnWriteArrayList<User>();
    private final List<User> topYearUsers = new CopyOnWriteArrayList<User>();

    @Override
    public Mono<List<User>> insertAndReturnOrderedByInterval(User topUser, TopUsersInterval interval) {
        insertAndReturnOrdered(topUser, this.topDayUsers);
        insertAndReturnOrdered(topUser, this.topMonthUsers);
        insertAndReturnOrdered(topUser, this.topYearUsers);

        //TODO: drop users that out of date range

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

    private void insertAndReturnOrdered(User topUser, List<User> topUsers) {
        topUsers.removeIf(user -> user.getId().equals(topUser.getId()));
        topUsers.add(topUser);
        this.sortAndTruncate(topUsers);
    }

    @Override
    public Mono<List<User>> setAndReturnOrdered(List<User> users, TopUsersInterval interval) {
        if (interval == TopUsersInterval.DAY) {
            this.topDayUsers.clear();
            this.topDayUsers.addAll(users);
            this.sortAndTruncate(this.topDayUsers);
            return Mono.just(this.topDayUsers.stream().limit(10).toList());
        }

        if (interval == TopUsersInterval.MONTH) {
            this.topMonthUsers.clear();
            this.topMonthUsers.addAll(users);
            this.sortAndTruncate(this.topMonthUsers);
            return Mono.just(this.topMonthUsers.stream().limit(10).toList());
        }

        if (interval == TopUsersInterval.YEAR) {
            this.topYearUsers.clear();
            this.topYearUsers.addAll(users);
            this.sortAndTruncate(this.topYearUsers);
            return Mono.just(this.topYearUsers.stream().limit(10).toList());
        }

        return Mono.empty();
    }

    private void sortAndTruncate(List<User> topUsers) {
        topUsers.sort(new Comparator<User>() {
            public int compare(User o1, User o2){
                if(o1.getChangesCount().equals(o2.getChangesCount()))
                    return 0;
                return o1.getChangesCount() > o2.getChangesCount() ? -1 : 1;
            }
        });
        while (topUsers.size() >= 10) {
            topUsers.remove(topUsers.size() - 1);
        }
    }
}
