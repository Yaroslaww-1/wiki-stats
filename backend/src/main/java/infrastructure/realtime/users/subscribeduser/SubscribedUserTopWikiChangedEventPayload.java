package infrastructure.realtime.users.subscribeduser;

import java.util.List;

record SubscribedUserTopWikiChangedEventPayloadWiki(
        Long changesCount,
        String wikiName
) { }

public record SubscribedUserTopWikiChangedEventPayload(
        String userId,
        List<SubscribedUserTopWikiChangedEventPayloadWiki> wikis
) { }