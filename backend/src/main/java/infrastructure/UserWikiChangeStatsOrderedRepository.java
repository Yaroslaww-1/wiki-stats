package infrastructure;

import application.changes.IUserWikiChangeStatsOrderedRepository;
import application.changes.UserWikiChangeStatsOrdered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class UserWikiChangeStatsOrderedRepository implements IUserWikiChangeStatsOrderedRepository {
    private final List<UserWikiChangeStatsOrdered> userWikiChangeStats = new CopyOnWriteArrayList<UserWikiChangeStatsOrdered>();

    @Override
    public Flux<UserWikiChangeStatsOrdered> insertAndReturnOrdered(String userId, UserWikiChangeStatsOrdered userWikiChangeStats) {
        //TODO: support userId. Use hashmap probably
        this.userWikiChangeStats.removeIf(stats ->
                stats.wikiName().equals(userWikiChangeStats.wikiName())
        );

        this.userWikiChangeStats.add(userWikiChangeStats);
        this.userWikiChangeStats.sort(new Comparator<UserWikiChangeStatsOrdered>() {
            public int compare(UserWikiChangeStatsOrdered o1, UserWikiChangeStatsOrdered o2){
                if(o1.changesCount().equals(o2.changesCount()))
                    return 0;
                return o1.changesCount() > o2.changesCount() ? -1 : 1;
            }
        });

        return this.set(userId, this.userWikiChangeStats.stream().limit(10).toList())
                .thenReturn(this.userWikiChangeStats)
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Void> set(String userId, List<UserWikiChangeStatsOrdered> userWikiChangeStats) {
        //TODO: support userId. Use hashmap probably
        this.userWikiChangeStats.clear();
        this.userWikiChangeStats.addAll(userWikiChangeStats);
        return Mono.empty();
    }
}
