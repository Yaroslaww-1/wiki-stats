package application.streaming.changes.steps.updateuserwikichangestats;

import domain.change.Change;

public record UpdateUserWikiChangeStatsStepInput(
        Change change
) { }
