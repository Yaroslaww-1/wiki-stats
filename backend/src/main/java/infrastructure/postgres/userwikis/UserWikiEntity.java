package infrastructure.postgres.userwikis;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_wikis")
public record UserWikiEntity(
        @Id String id,
        Long changesCount,
        String userId,
        String wikiId,
        String wikiName
) { }
