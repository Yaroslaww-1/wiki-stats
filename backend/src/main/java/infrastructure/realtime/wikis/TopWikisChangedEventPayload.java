package infrastructure.realtime.wikis;

import java.util.List;

record TopWikisChangedEventPayloadWiki(
        String wikiName,
        Long editsCount
) {}

public record TopWikisChangedEventPayload(
        List<TopWikisChangedEventPayloadWiki> wikis
) { }