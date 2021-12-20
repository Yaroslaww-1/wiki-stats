package api.controllers.users;

import application.users.getlist.GetUsersListQuery;
import application.users.getlist.GetUsersListQueryHandler;
import application.users.getlist.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users")
public class UsersController {
    @GetMapping("")
    private Flux<UserDto> getAll(
            @RequestParam("page") Integer page,
            @RequestParam("count") Integer count
    ) {
        var query = new GetUsersListQuery(page, count);
        return new GetUsersListQueryHandler().execute(query);
    }
}

