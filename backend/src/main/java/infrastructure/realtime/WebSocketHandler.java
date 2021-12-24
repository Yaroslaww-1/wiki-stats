package infrastructure.realtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
                .doOnNext(message -> logger.info("Send WS message: " + message));
        return session.send(messages);
    }

    private Mono<String> mapEventToString(Event event) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            return Mono.just(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}