package application.streaming.changes.steps.updatesubscribeduserinfo;

import domain.change.Change;
import domain.user.User;
import domain.wiki.Wiki;

public record UpdateSubscribedUserInfoStepInput(
    User user,
    Wiki wiki,
    Change change
) { }
