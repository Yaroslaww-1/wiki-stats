package api.controllers.users;

import application.crud.users.getlist.GetUsersListQuery;
import application.crud.users.getlist.GetUsersListQueryHandler;
import application.crud.users.getlist.UserDto;
import application.crud.users.getstats.GetUsersStatsQuery;
import application.crud.users.getstats.GetUsersStatsQueryHandler;
import application.crud.users.getstats.UsersStatsDto;
import application.crud.users.getuserchangesstats.GetUserChangesStatsQuery;
import application.crud.users.getuserchangesstats.GetUserChangesStatsQueryHandler;
import application.crud.users.getuserchangesstats.UserChangesStatsDto;
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

    @Autowired
    public UsersController(
            GetUsersListQueryHandler getUsersListQueryHandler,
            GetUsersStatsQueryHandler getUsersStatsQueryHandler,
            GetUserChangesStatsQueryHandler getUserChangesStatsQueryHandler
    ) {
        this.getUsersListQueryHandler = getUsersListQueryHandler;
        this.getUsersStatsQueryHandler = getUsersStatsQueryHandler;
        this.getUserChangesStatsQueryHandler = getUserChangesStatsQueryHandler;
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

