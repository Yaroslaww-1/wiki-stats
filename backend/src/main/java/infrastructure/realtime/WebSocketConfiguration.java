package infrastructure.realtime;

import infrastructure.realtime.users.UserEventsRealtimeNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
public class WebSocketConfiguration {
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public WebSocketConfiguration(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = Map.of("/", webSocketHandler);
        return new SimpleUrlHandlerMapping(map, -1);
    }

    @Bean
    public HandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}