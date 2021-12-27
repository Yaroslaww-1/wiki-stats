package application.streaming.changes.steps.updateuserchangestats;

import domain.change.Change;

public record UpdateUserChangeStatsStepInput(
        Change change
) { }
