package infrastructure.postgres.userwikichangestats;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_wiki_change_stats")
public record UserWikiChangeStatsEntity(
        @Id String id,
        Long changesCount,
        String userId,
        String wikiId
) { }