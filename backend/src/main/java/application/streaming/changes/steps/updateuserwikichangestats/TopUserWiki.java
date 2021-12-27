package application.streaming.changes.steps.updateuserwikichangestats;

public record TopUserWiki(
        Long changesCount,
        String wikiName
) {}