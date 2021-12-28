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
        this.topWikis.sort(new Comparator<Wiki>() {
            public int compare(Wiki o1, Wiki o2){
                if(o1.getEditsCount().equals(o2.getEditsCount()))
                    return 0;
                return o1.getEditsCount() > o2.getEditsCount() ? -1 : 1;
            }
        });

        while (this.topWikis.size() >= 10) {
            this.topWikis.remove(this.topWikis.size() - 1);
        }

        return Mono.just(this.topWikis.stream().toList());
    }
}
