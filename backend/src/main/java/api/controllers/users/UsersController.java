package api.controllers.users;

import api.utils.PagingInfoConstructor;
import application.users.getlist.GetUsersListQuery;
import application.users.getlist.GetUsersListQueryHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;


public class UsersController {
    public Publisher<Void> getUsersRoute(HttpServerRequest request, HttpServerResponse response) {
        var paging = new PagingInfoConstructor(request);
        var usersFlux = new GetUsersListQueryHandler().execute(new GetUsersListQuery(paging.page, paging.count));

        var users = usersFlux.buffer().single().share().block();

        try {
            var mapper = new ObjectMapper();
            var str = mapper.writeValueAsString(users);
            response.header("content-type", "application/json");
            return response.sendString(Mono.just(str));
        } catch (JsonProcessingException ex) {
            return response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
