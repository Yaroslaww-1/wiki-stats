package api.controllers.admin;

import application.admin.resetsession.ResetSessionCommand;
import application.admin.resetsession.ResetSessionCommandHandler;
import application.admin.setchangesprocessingdelay.SetChangesProcessingDelayCommand;
import application.admin.setchangesprocessingdelay.SetChangesProcessingDelayCommandHandler;
import application.admin.settopusersinterval.SetTopUsersIntervalCommand;
import application.admin.settopusersinterval.SetTopUsersIntervalCommandHandler;
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
    private final SetTopUsersIntervalCommandHandler setTopUsersIntervalCommandHandler;

    @Autowired
    public AdminController(
            ResetSessionCommandHandler resetSessionCommandHandler,
            SetChangesProcessingDelayCommandHandler setChangesProcessingDelayCommandHandler,
            SubscribeForUserChangesCommandHandler subscribeForUserChangesCommandHandler,
            SetTopUsersIntervalCommandHandler setTopUsersIntervalCommandHandler
    ) {
        this.resetSessionCommandHandler = resetSessionCommandHandler;
        this.setChangesProcessingDelayCommandHandler = setChangesProcessingDelayCommandHandler;
        this.subscribeForUserChangesCommandHandler = subscribeForUserChangesCommandHandler;
        this.setTopUsersIntervalCommandHandler = setTopUsersIntervalCommandHandler;
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

    @PutMapping("interval")
    private Mono<Void> putInterval(@RequestBody() SetTopUsersIntervalRequest request) {
        var command = new SetTopUsersIntervalCommand(request.interval());
        return setTopUsersIntervalCommandHandler.execute(command);
    }

    @PostMapping("{userName}/subscribe")
    private Mono<Void> postSubscribe(@PathVariable String userName) {
        var command = new SubscribeForUserChangesCommand(userName);
        return subscribeForUserChangesCommandHandler.execute(command);
    }
}