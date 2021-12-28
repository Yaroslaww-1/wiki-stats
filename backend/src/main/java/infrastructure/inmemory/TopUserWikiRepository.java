package infrastructure.inmemory;

import domain.userwiki.ITopUserWikiRepository;
import domain.userwiki.UserWiki;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopUserWikiRepository implements ITopUserWikiRepository {
    private final List<UserWiki> userWikiStats = new CopyOnWriteArrayList<UserWiki>();

    @Override
    public Mono<List<UserWiki>> insertAndReturnOrdered(String userId, UserWiki userWiki) {
        //TODO: support userId. Use hashmap probably
        this.userWikiStats.removeIf(uw -> uw.getWikiId().equals(userWiki.getWikiId()));

        this.userWikiStats.add(userWiki);
        this.userWikiStats.sort(new Comparator<UserWiki>() {
            public int compare(UserWiki o1, UserWiki o2){
                if(o1.getChangesCount().equals(o2.getChangesCount()))
                    return 0;
                return o1.getChangesCount() > o2.getChangesCount() ? -1 : 1;
            }
        });

        while (this.userWikiStats.size() >= 10) {
            this.userWikiStats.remove(this.userWikiStats.size() - 1);
        }

        return Mono.just(this.userWikiStats.stream().toList());
    }
}
