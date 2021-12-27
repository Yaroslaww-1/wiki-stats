package infrastructure.realtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WebSocketHandler implements org.springframework.web.reactive.socket.WebSocketHandler {
    private final IRealtimeNotifier realtimeNotifier;
    private final ObjectMapper objectMapper;
    private final Logger logger;

    @Autowired
    public WebSocketHandler(IRealtimeNotifier realtimeNotifier, ObjectMapper objectMapper, Logger logger) {
        this.realtimeNotifier = realtimeNotifier;
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messages = session.receive()
                .flatMap(__ -> realtimeNotifier.getEvents())
                .flatMap(this::mapEventToString)
                .map(session::textMessage)
                .doOnError(throwable -> logger.error("Exception during sending WS event: " + throwable.getMessage()));
        return session.send(messages);
    }

    private Mono<String> mapEventToString(Event event) {
        try {
            return Mono.just(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}