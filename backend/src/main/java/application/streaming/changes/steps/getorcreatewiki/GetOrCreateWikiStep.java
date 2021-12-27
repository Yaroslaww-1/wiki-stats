package application.streaming.changes.steps.getorcreatewiki;

import application.crud.wikis.IWikiEventsRealtimeNotifier;
import application.crud.wikis.IWikiRepository;
import application.streaming.contracts.IStep;
import domain.wiki.Wiki;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Component
public class GetOrCreateWikiStep implements IStep<GetOrCreateWikiStepInput, Wiki> {
    private final IWikiRepository wikiRepository;
    private final IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier;

    public GetOrCreateWikiStep(
            IWikiRepository wikiRepository,
            IWikiEventsRealtimeNotifier wikiEventsRealtimeNotifier
    ) {
        this.wikiRepository = wikiRepository;
        this.wikiEventsRealtimeNotifier = wikiEventsRealtimeNotifier;
    }

    @Override
    public Mono<Wiki> execute(GetOrCreateWikiStepInput input) {
        return wikiRepository
                    .getOne(query(where("name").is(input.wikiName())))
                    .switchIfEmpty(this.createWiki(input.wikiName()));
    }

    private Mono<Wiki> createWiki(String name) {
        return Mono
                .just(new Wiki(name))
                .delayUntil(wikiRepository::add)
                .delayUntil(wikiEventsRealtimeNotifier::notifyWikiCreated);
    }
}
