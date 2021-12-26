package api.controllers.changes;

import application.changes.setprocessingdelay.SetProcessingDelayCommand;
import application.changes.setprocessingdelay.SetProcessingDelayCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/changes")
public class ChangesController {
    private final SetProcessingDelayCommandHandler setProcessingDelayCommandHandler;

    @Autowired
    public ChangesController(
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

