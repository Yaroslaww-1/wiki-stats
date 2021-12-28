package application.streaming.changes.steps.updatestatsandtops;

import domain.change.Change;
import domain.user.User;
import domain.wiki.Wiki;

public record UpdateStatsAndTopsStepInput(
        Change change,
        User user,
        Wiki wiki
) { }
