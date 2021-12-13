package api;

import api.controllers.users.UsersController;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

public class WikiStatsAPI {
    public static void main(String[] args) {
        UsersController usersController = new UsersController();

        DisposableServer server = HttpServer
                .create()
                .host("localhost")
                .port(8000)
                .route(routes ->
                    routes.get("/users", usersController::getUsersRoute))
                .bindNow();

        server
            .onDispose()
            .block();
    }
}
