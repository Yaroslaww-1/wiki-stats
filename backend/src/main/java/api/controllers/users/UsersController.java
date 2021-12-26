package api.controllers.users;

import application.users.getlist.GetUsersListQuery;
import application.users.getlist.GetUsersListQueryHandler;
import application.users.getlist.UserDto;
import application.users.getstats.GetUsersStatsQuery;
import application.users.getstats.GetUsersStatsQueryHandler;
import application.users.getstats.UsersStatsDto;
import application.users.getusereditsstats.GetUserEditsStatsQuery;
import application.users.getusereditsstats.GetUserEditsStatsQueryHandler;
import application.users.getusereditsstats.UserEditsStatsDto;
import application.users.subscribeforuseredits.SubscribeForUserEditsCommand;
import application.users.subscribeforuseredits.SubscribeForUserEditsCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UsersController {
    private final GetUsersListQueryHandler getUsersListQueryHandler;
    private final GetUsersStatsQueryHandler getUsersStatsQueryHandler;
    private final GetUserEditsStatsQueryHandler getUserEditsStatsQueryHandler;
    private final SubscribeForUserEditsCommandHandler subscribeForUserEditsCommandHandler;

    @Autowired
    public UsersController(
            GetUsersListQueryHandler getUsersListQueryHandler,
            GetUsersStatsQueryHandler getUsersStatsQueryHandler,
            GetUserEditsStatsQueryHandler getUserEditsStatsQueryHandler,
            SubscribeForUserEditsCommandHandler subscribeForUserEditsCommandHandler) {
        this.getUsersListQueryHandler = getUsersListQueryHandler;
        this.getUsersStatsQueryHandler = getUsersStatsQueryHandler;
        this.getUserEditsStatsQueryHandler = getUserEditsStatsQueryHandler;
        this.subscribeForUserEditsCommandHandler = subscribeForUserEditsCommandHandler;
    }

    @GetMapping("")
    private Flux<UserDto> getAll(
            @RequestParam("page") Integer page,
            @RequestParam("count") Integer count,
            @RequestParam("name") String name
    ) {
        var query = new GetUsersListQuery(page, count, name);
        return getUsersListQueryHandler.execute(query);
    }

    @GetMapping("stats")
    private Mono<UsersStatsDto> getStats() {
        var query = new GetUsersStatsQuery();
        return getUsersStatsQueryHandler.execute(query);
    }

    @PostMapping("{userName}/subscribe")
    private Mono<Void> postSubscribe(@PathVariable String userName) {
        var command = new SubscribeForUserEditsCommand(userName);
        return subscribeForUserEditsCommandHandler.execute(command);
    }

    @GetMapping("{userName}/stats")
    private Mono<UserEditsStatsDto> getUserEditsStats(
            @PathVariable String userName,
            @RequestParam Long window
    ) {
        var query = new GetUserEditsStatsQuery(userName, window);
        return getUserEditsStatsQueryHandler.execute(query);
    }
}

