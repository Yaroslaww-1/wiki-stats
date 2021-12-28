package application.streaming.changes.flows.addchange;

import application.crud.admin.session.ISessionRepository;
import application.streaming.changes.steps.createchange.CreateChangeStep;
import application.streaming.changes.steps.createchange.CreateChangeStepInput;
import application.streaming.changes.steps.getorcreateuser.GetOrCreateUserStep;
import application.streaming.changes.steps.getorcreateuser.GetOrCreateUserStepInput;
import application.streaming.changes.steps.getorcreatewiki.GetOrCreateWikiStep;
import application.streaming.changes.steps.getorcreatewiki.GetOrCreateWikiStepInput;
import application.streaming.changes.steps.updatestatsandtops.UpdateStatsAndTopsStep;
import application.streaming.changes.steps.updatestatsandtops.UpdateStatsAndTopsStepInput;
import application.streaming.changes.steps.updatesubscribeduserinfo.UpdateSubscribedUserInfoStep;
import application.streaming.changes.steps.updatesubscribeduserinfo.UpdateSubscribedUserInfoStepInput;
import application.streaming.contracts.IFlow;
import domain.change.Change;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.Duration;

@Component
public class AddChangeFlow implements IFlow<AddChangeFlowInput, Change> {
    private final GetOrCreateUserStep getOrCreateUserStep;
    private final GetOrCreateWikiStep getOrCreateWikiStep;
    private final CreateChangeStep createChangeStep;
    private final UpdateStatsAndTopsStep updateStatsAndTopsStep;
    private final UpdateSubscribedUserInfoStep updateSubscribedUserInfoStep;
    private final ISessionRepository sessionRepository;

    @Autowired
    public AddChangeFlow(
            GetOrCreateUserStep getOrCreateUserStep,
            GetOrCreateWikiStep getOrCreateWikiStep,
            CreateChangeStep createChangeStep,
            UpdateStatsAndTopsStep updateStatsAndTopsStep,
            UpdateSubscribedUserInfoStep updateSubscribedUserInfoStep,
            ISessionRepository sessionRepository
    ) {
        this.getOrCreateUserStep = getOrCreateUserStep;
        this.getOrCreateWikiStep = getOrCreateWikiStep;
        this.createChangeStep = createChangeStep;
        this.updateStatsAndTopsStep = updateStatsAndTopsStep;
        this.updateSubscribedUserInfoStep = updateSubscribedUserInfoStep;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Flux<Change> run(Flux<AddChangeFlowInput> inputFlux) {
        return inputFlux
                .filter(change -> change.type().equals("edit") || change.type().equals("new"))
//                .delayElements(Duration.ofMillis(75))
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
                .delayUntil(tuple -> updateStatsAndTopsStep.execute(
                        new UpdateStatsAndTopsStepInput(tuple.getT3(), tuple.getT1(), tuple.getT2())
                ))
                .filter(tuple -> sessionRepository.isSubscribedForUserChanges(tuple.getT1().getId()))
                .delayUntil(tuple -> updateSubscribedUserInfoStep.execute(
                        new UpdateSubscribedUserInfoStepInput(tuple.getT1(), tuple.getT2(), tuple.getT3())
                ))
                .map(Tuple3::getT3);
    }
}
