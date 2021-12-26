package api.controllers.edits;

import application.edits.setprocessingdelay.SetProcessingDelayCommand;
import application.edits.setprocessingdelay.SetProcessingDelayCommandHandler;
import application.users.subscribeforuseredits.SubscribeForUserEditsCommand;
import application.users.subscribeforuseredits.SubscribeForUserEditsCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/edits")
public class EditsController {
    private final SetProcessingDelayCommandHandler setProcessingDelayCommandHandler;
    private final SubscribeForUserEditsCommandHandler subscribeForUserEditsCommandHandler;

    @Autowired
    public EditsController(
            SetProcessingDelayCommandHandler setProcessingDelayCommandHandler,
            SubscribeForUserEditsCommandHandler subscribeForUserEditsCommandHandler
    ) {
        this.setProcessingDelayCommandHandler = setProcessingDelayCommandHandler;
        this.subscribeForUserEditsCommandHandler = subscribeForUserEditsCommandHandler;
    }

    @PutMapping("delay")
    private Mono<Void> putDelay(@RequestBody() SetDelayRequest request) {
        var command = new SetProcessingDelayCommand(request.delay());
        return setProcessingDelayCommandHandler.execute(command);
    }

    @PostMapping("subscribe")
    private Mono<Void> postSubscribe(@RequestBody() SubscribeForUserEditsRequest request) {
        var command = new SubscribeForUserEditsCommand(request.userName());
        return subscribeForUserEditsCommandHandler.execute(command);
    }
}

