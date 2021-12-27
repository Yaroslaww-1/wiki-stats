package infrastructure.realtime.changes;

import java.util.List;

record UserWikiChangeStatsChangedEventItemPayload(
        Long changesCount,
        String wikiName
) { }

public record UserWikiChangeStatsChangedEventPayload(
        String userId,
        List<UserWikiChangeStatsChangedEventItemPayload> wikis
) { }