package infrastructure;

import application.users.topusers.ITopUsersRepository;
import application.users.topusers.TopUsers;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class TopUsersRepository implements ITopUsersRepository {
    private final List<TopUsers> topUsers = new CopyOnWriteArrayList<TopUsers>();

    @Override
    public Flux<TopUsers> insertAndReturnOrdered(String userName, Long changesCount) {
        this.topUsers.removeIf(stats -> stats.userName().equals(userName));

        this.topUsers.add(new TopUsers(changesCount, userName));
        this.topUsers.sort(new Comparator<TopUsers>() {
            public int compare(TopUsers o1, TopUsers o2){
                if(o1.changesCount().equals(o2.changesCount()))
                    return 0;
                return o1.changesCount() > o2.changesCount() ? -1 : 1;
            }
        });

        return this.set(this.topUsers.stream().limit(10).toList())
                .thenReturn(this.topUsers)
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Void> set(List<TopUsers> topUsers) {
        this.topUsers.clear();
        this.topUsers.addAll(topUsers);
        return Mono.empty();
    }
}
