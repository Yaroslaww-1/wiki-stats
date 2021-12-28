package infrastructure.inmemory;

import domain.wiki.ITopWikiRepository;
import domain.wiki.Wiki;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopWikiRepository implements ITopWikiRepository {
    private final List<Wiki> topWikis = new CopyOnWriteArrayList<Wiki>();

    @Override
    public Mono<List<Wiki>> insertAndReturnOrdered(Wiki wiki) {
        this.topWikis.removeIf(w -> w.getId().equals(wiki.getId()));

        this.topWikis.add(wiki);
        this.sortAndTruncate(this.topWikis);

        return Mono.just(this.topWikis.stream().toList());
    }

    @Override
    public Mono<List<Wiki>> setAndReturnOrdered(List<Wiki> wikis) {
        this.topWikis.clear();
        this.topWikis.addAll(wikis);
        this.sortAndTruncate(this.topWikis);
        return Mono.just(this.topWikis.stream().limit(10).toList());
    }

    private void sortAndTruncate(List<Wiki> topWikis) {
        topWikis.sort(new Comparator<Wiki>() {
            public int compare(Wiki o1, Wiki o2){
                if(o1.getEditsCount().equals(o2.getEditsCount()))
                    return 0;
                return o1.getEditsCount() > o2.getEditsCount() ? -1 : 1;
            }
        });

        while (topWikis.size() >= 10) {
            topWikis.remove(topWikis.size() - 1);
        }
    }
}
