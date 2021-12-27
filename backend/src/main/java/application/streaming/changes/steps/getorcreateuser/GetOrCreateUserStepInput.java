package application.streaming.changes.steps.getorcreateuser;

public record GetOrCreateUserStepInput(
        String userName,
        Boolean isBot
) { }
