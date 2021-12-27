package api.controllers.admin;

import application.admin.resetsession.ResetSessionCommand;
import application.admin.resetsession.ResetSessionCommandHandler;
import application.admin.setchangesprocessingdelay.SetChangesProcessingDelayCommand;
import application.admin.setchangesprocessingdelay.SetChangesProcessingDelayCommandHandler;
import application.admin.subscribeforuserchanges.SubscribeForUserChangesCommand;
import application.admin.subscribeforuserchanges.SubscribeForUserChangesCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ResetSessionCommandHandler resetSessionCommandHandler;
    private final SetChangesProcessingDelayCommandHandler setChangesProcessingDelayCommandHandler;
    private final SubscribeForUserChangesCommandHandler subscribeForUserChangesCommandHandler;

    @Autowired
    public AdminController(
            ResetSessionCommandHandler resetSessionCommandHandler,
            SetChangesProcessingDelayCommandHandler setChangesProcessingDelayCommandHandler,
            SubscribeForUserChangesCommandHandler subscribeForUserChangesCommandHandler
    ) {
        this.resetSessionCommandHandler = resetSessionCommandHandler;
        this.setChangesProcessingDelayCommandHandler = setChangesProcessingDelayCommandHandler;
        this.subscribeForUserChangesCommandHandler = subscribeForUserChangesCommandHandler;
    }

    @PostMapping("reset")
    private Mono<Void> postReset() {
        var command = new ResetSessionCommand();
        return resetSessionCommandHandler.execute(command);
    }

    @PutMapping("delay")
    private Mono<Void> putDelay(@RequestBody() SetDelayRequest request) {
        var command = new SetChangesProcessingDelayCommand(request.delay());
        return setChangesProcessingDelayCommandHandler.execute(command);
    }

    @PostMapping("{userName}/subscribe")
    private Mono<Void> postSubscribe(@PathVariable String userName) {
        var command = new SubscribeForUserChangesCommand(userName);
        return subscribeForUserChangesCommandHandler.execute(command);
    }
}