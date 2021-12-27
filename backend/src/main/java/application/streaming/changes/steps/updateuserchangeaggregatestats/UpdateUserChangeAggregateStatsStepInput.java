package application.streaming.changes.steps.updateuserchangeaggregatestats;

import domain.change.Change;

public record UpdateUserChangeAggregateStatsStepInput(
    Change change
) { }
