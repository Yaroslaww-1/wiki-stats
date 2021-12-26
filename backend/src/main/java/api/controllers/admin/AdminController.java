package api.controllers.admin;

import application.admin.resetstate.ResetStateCommand;
import application.admin.resetstate.ResetStateCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ResetStateCommandHandler resetStateCommandHandler;

    @Autowired
    public AdminController(
            ResetStateCommandHandler resetStateCommandHandler
    ) {
        this.resetStateCommandHandler = resetStateCommandHandler;
    }

    @PostMapping("reset")
    private Mono<Void> postReset() {
        var command = new ResetStateCommand();
        return resetStateCommandHandler.execute(command);
    }
}