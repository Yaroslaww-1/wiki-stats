package infrastructure.inmemory;

import domain.user.User;
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
        this.sortAndTruncate(this.userWikiStats);

        return Mono.just(this.userWikiStats.stream().toList());
    }

    @Override
    public Mono<List<UserWiki>> setAndReturnOrdered(String userId, List<UserWiki> userWikis) {
        this.userWikiStats.clear();
        this.userWikiStats.addAll(userWikis);
        this.sortAndTruncate(this.userWikiStats);
        return Mono.just(this.userWikiStats.stream().limit(10).toList());
    }

    private void sortAndTruncate(List<UserWiki> topUserWikis) {
        topUserWikis.sort(new Comparator<UserWiki>() {
            public int compare(UserWiki o1, UserWiki o2){
                if(o1.getChangesCount().equals(o2.getChangesCount()))
                    return 0;
                return o1.getChangesCount() > o2.getChangesCount() ? -1 : 1;
            }
        });

        while (topUserWikis.size() >= 10) {
            topUserWikis.remove(topUserWikis.size() - 1);
        }
    }
}
