package application.wikis.getstats;

import application.contracts.IQueryHandler;
import application.users.getstats.UsersStatsDto;
import application.wikis.IWikiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetWikisStatsQueryHandler implements IQueryHandler<GetWikisStatsQuery, WikisStatsDto> {
    private final IWikiRepository wikiRepository;

    @Autowired
    public GetWikisStatsQueryHandler(IWikiRepository wikiRepository) {
        this.wikiRepository = wikiRepository;
    }

    @Override
    public Mono<WikisStatsDto> execute(GetWikisStatsQuery query) {
        return wikiRepository.getTotalCount()
                .map(WikisStatsDto::new);
    }
}