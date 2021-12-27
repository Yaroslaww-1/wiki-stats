package api.controllers.wikis;

import application.crud.wikis.getstats.GetWikisStatsQuery;
import application.crud.wikis.getstats.GetWikisStatsQueryHandler;
import application.crud.wikis.getstats.WikisStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wikis")
@CrossOrigin(origins = "*")
public class WikisController {
    private final GetWikisStatsQueryHandler getWikisStatsQueryHandler;

    @Autowired
    public WikisController(
            GetWikisStatsQueryHandler getWikisStatsQueryHandler
    ) {
        this.getWikisStatsQueryHandler = getWikisStatsQueryHandler;
    }

    @GetMapping("stats")
    private Mono<WikisStatsDto> getStats() {
        var query = new GetWikisStatsQuery();
        return getWikisStatsQueryHandler.execute(query);
    }
}

