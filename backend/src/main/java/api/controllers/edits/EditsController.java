package api.controllers.edits;

import application.edits.setprocessingdelay.SetProcessingDelayCommand;
import application.edits.setprocessingdelay.SetProcessingDelayCommandHandler;
import application.users.subscribeforuseredits.SubscribeForUserEditsCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/edits")
public class EditsController {
    private final SetProcessingDelayCommandHandler setProcessingDelayCommandHandler;

    @Autowired
    public EditsController(
            SetProcessingDelayCommandHandler setProcessingDelayCommandHandler
    ) {
        this.setProcessingDelayCommandHandler = setProcessingDelayCommandHandler;
    }

    @PutMapping("delay")
    private Mono<Void> putDelay(@RequestBody() SetDelayRequest request) {
        var command = new SetProcessingDelayCommand(request.delay());
        return setProcessingDelayCommandHandler.execute(command);
    }
}

