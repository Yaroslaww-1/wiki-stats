package application.changes;

public record UserWikiChangeStatsOrdered(
        Long changesCount,
        String wikiName
) {}