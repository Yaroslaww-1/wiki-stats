package application.streaming.changes.steps.createchange;

import domain.user.User;
import domain.wiki.Wiki;

import java.time.LocalDateTime;

public record CreateChangeStepInput(
        User user,
        Wiki wiki,
        String id,
        LocalDateTime timestamp,
        String title,
        String comment,
        String type
) { }
