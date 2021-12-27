package infrastructure;

import application.streaming.changes.steps.updateuserwikichangestats.ITopUserWikiRepository;
import application.streaming.changes.steps.updateuserwikichangestats.TopUserWiki;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopUserWikiRepository implements ITopUserWikiRepository {
    private final List<TopUserWiki> userWikiChangeStats = new CopyOnWriteArrayList<TopUserWiki>();

    @Override
    public Mono<List<TopUserWiki>> insertAndReturnOrdered(String userId, TopUserWiki userWikiChangeStats) {
        //TODO: support userId. Use hashmap probably
        this.userWikiChangeStats.removeIf(stats ->
                stats.wikiName().equals(userWikiChangeStats.wikiName())
        );

        this.userWikiChangeStats.add(userWikiChangeStats);
        this.userWikiChangeStats.sort(new Comparator<TopUserWiki>() {
            public int compare(TopUserWiki o1, TopUserWiki o2){
                if(o1.changesCount().equals(o2.changesCount()))
                    return 0;
                return o1.changesCount() > o2.changesCount() ? -1 : 1;
            }
        });

        while (this.userWikiChangeStats.size() >= 10) {
            this.userWikiChangeStats.remove(this.userWikiChangeStats.size() - 1);
        }

        return Mono.just(this.userWikiChangeStats.stream().toList());
    }

    @Override
    public Mono<Void> set(String userId, List<TopUserWiki> userWikiChangeStats) {
        //TODO: support userId. Use hashmap probably
        this.userWikiChangeStats.clear();
        this.userWikiChangeStats.addAll(userWikiChangeStats);
        return Mono.empty();
    }
}
