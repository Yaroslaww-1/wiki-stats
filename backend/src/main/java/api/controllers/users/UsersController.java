package api.controllers.users;

import application.users.getlist.GetUsersListQuery;
import application.users.getlist.GetUsersListQueryHandler;
import application.users.getlist.UserDto;
import application.users.getstats.GetUsersStatsQuery;
import application.users.getstats.GetUsersStatsQueryHandler;
import application.users.getstats.UsersStatsDto;
import application.users.getuserchangesstats.GetUserChangesStatsQuery;
import application.users.getuserchangesstats.GetUserChangesStatsQueryHandler;
import application.users.getuserchangesstats.UserChangesStatsDto;
import application.users.subscribeforuserchanges.SubscribeForUserChangesCommand;
import application.users.subscribeforuserchanges.SubscribeForUserChangesCommandHandler;
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
    private final GetUserChangesStatsQueryHandler getUserChangesStatsQueryHandler;
    private final SubscribeForUserChangesCommandHandler subscribeForUserChangesCommandHandler;

    @Autowired
    public UsersController(
            GetUsersListQueryHandler getUsersListQueryHandler,
            GetUsersStatsQueryHandler getUsersStatsQueryHandler,
            GetUserChangesStatsQueryHandler getUserChangesStatsQueryHandler,
            SubscribeForUserChangesCommandHandler subscribeForUserChangesCommandHandler) {
        this.getUsersListQueryHandler = getUsersListQueryHandler;
        this.getUsersStatsQueryHandler = getUsersStatsQueryHandler;
        this.getUserChangesStatsQueryHandler = getUserChangesStatsQueryHandler;
        this.subscribeForUserChangesCommandHandler = subscribeForUserChangesCommandHandler;
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
        var command = new SubscribeForUserChangesCommand(userName);
        return subscribeForUserChangesCommandHandler.execute(command);
    }

    @GetMapping("{userName}/stats")
    private Mono<UserChangesStatsDto> getUserChangesStats(
            @PathVariable String userName,
            @RequestParam Long window,
            @RequestParam Long step
    ) {
        var query = new GetUserChangesStatsQuery(userName, window, step);
        return getUserChangesStatsQueryHandler.execute(query);
    }
}

