package application.streaming.changes.flows.addchange;

import application.crud.admin.session.ISessionRepository;
import application.streaming.changes.steps.createchange.CreateChangeStep;
import application.streaming.changes.steps.createchange.CreateChangeStepInput;
import application.streaming.changes.steps.getorcreateuser.GetOrCreateUserStep;
import application.streaming.changes.steps.getorcreateuser.GetOrCreateUserStepInput;
import application.streaming.changes.steps.getorcreatewiki.GetOrCreateWikiStep;
import application.streaming.changes.steps.getorcreatewiki.GetOrCreateWikiStepInput;
import application.streaming.changes.steps.updateuserchangeaggregatestats.UpdateUserChangeAggregateStatsStep;
import application.streaming.changes.steps.updateuserchangeaggregatestats.UpdateUserChangeAggregateStatsStepInput;
import application.streaming.changes.steps.updateuserchangestats.UpdateUserChangeStatsStep;
import application.streaming.changes.steps.updateuserchangestats.UpdateUserChangeStatsStepInput;
import application.streaming.changes.steps.updateuserwikichangestats.UpdateUserWikiChangeStatsStep;
import application.streaming.changes.steps.updateuserwikichangestats.UpdateUserWikiChangeStatsStepInput;
import application.streaming.contracts.IFlow;
import domain.change.Change;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class AddChangeFlow implements IFlow<AddChangeFlowInput, Change> {
    private final GetOrCreateUserStep getOrCreateUserStep;
    private final GetOrCreateWikiStep getOrCreateWikiStep;
    private final CreateChangeStep createChangeStep;
    private final UpdateUserChangeStatsStep updateUserChangeStatsStep;
    private final UpdateUserWikiChangeStatsStep updateUserWikiChangeStatsStep;
    private final UpdateUserChangeAggregateStatsStep updateUserChangeAggregateStatsStep;

    @Autowired
    public AddChangeFlow(
            GetOrCreateUserStep getOrCreateUserStep,
            GetOrCreateWikiStep getOrCreateWikiStep,
            CreateChangeStep createChangeStep,
            UpdateUserChangeStatsStep updateUserChangeStatsStep,
            UpdateUserWikiChangeStatsStep updateUserWikiChangeStatsStep,
            UpdateUserChangeAggregateStatsStep updateUserChangeAggregateStatsStep
    ) {
        this.getOrCreateUserStep = getOrCreateUserStep;
        this.getOrCreateWikiStep = getOrCreateWikiStep;
        this.createChangeStep = createChangeStep;
        this.updateUserChangeStatsStep = updateUserChangeStatsStep;
        this.updateUserWikiChangeStatsStep = updateUserWikiChangeStatsStep;
        this.updateUserChangeAggregateStatsStep = updateUserChangeAggregateStatsStep;
    }

    @Override
    public Flux<Change> run(Flux<AddChangeFlowInput> inputFlux) {
        return inputFlux
                .delayElements(Duration.ofMillis(75))
                .flatMap(input ->
                    Flux.zip(
                        getOrCreateUserStep.execute(new GetOrCreateUserStepInput(input.editor(), input.isBot())),
                        getOrCreateWikiStep.execute(new GetOrCreateWikiStepInput(input.wiki())),
                        Mono.just(input)
                    ), 1
                )
                .map(tuple -> {
                    var user = tuple.getT1();
                    var wiki = tuple.getT2();
                    var input = tuple.getT3();

                    return new CreateChangeStepInput(
                            user,
                            wiki,
                            input.id(),
                            input.timestamp(),
                            input.title(),
                            input.comment(),
                            input.type()
                    );
                })
                .flatMap(createChangeStep::execute, 1)
                .filter(change -> change.isAdd() || change.isEdit())
                .delayUntil(change -> updateUserChangeStatsStep.execute(new UpdateUserChangeStatsStepInput(change)))
                .delayUntil(change -> updateUserWikiChangeStatsStep.execute(new UpdateUserWikiChangeStatsStepInput(change)))
                .delayUntil(change -> updateUserChangeAggregateStatsStep.execute(new UpdateUserChangeAggregateStatsStepInput(change)));
    }
}
