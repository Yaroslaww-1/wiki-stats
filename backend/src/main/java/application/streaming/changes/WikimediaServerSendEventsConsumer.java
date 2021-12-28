package application.streaming.changes;

import application.crud.admin.session.ISessionRepository;
import application.streaming.changes.flows.addchange.AddChangeFlow;
import application.streaming.changes.flows.addchange.AddChangeFlowInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import infrastructure.ExternalServerSentEventsConsumer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Component
public class WikimediaServerSendEventsConsumer {
    private final Logger logger;
    private final AddChangeFlow addChangeFlow;
    private final ISessionRepository sessionRepository;

    @Autowired
    public WikimediaServerSendEventsConsumer(
            AddChangeFlow addChangeFlow,
            Logger logger,
            ISessionRepository sessionRepository
    ) {
        this.logger = logger;
        this.addChangeFlow = addChangeFlow;
        this.sessionRepository = sessionRepository;
    }

    public Flux startConsuming() {
        var url = "https://stream.wikimedia.org/v2/stream/recentchange";
        var consumer = new ExternalServerSentEventsConsumer(url, "/");
        return consumer.startConsuming()
                .onBackpressureDrop() //TODO: probably not the best solution
                .mapNotNull(ServerSentEvent::data)
                .flatMap(eventData -> this.mapToAddChangeFlowInput(eventData)
                        .doOnError(throwable -> logger.error("Exception during parsing wikimedia event: " + throwable.getMessage()))
                        .onErrorResume(__ -> Mono.empty()),
                        10
                )
                .transform(addChangeFlow::run);
    }

    private Mono<AddChangeFlowInput> mapToAddChangeFlowInput(String eventData) {
        final ObjectNode node;

        try {
            node = new ObjectMapper().readValue(eventData, ObjectNode.class);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }

        try {
            var input = new AddChangeFlowInput(
                    node.get("id").asText(),
                    LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(Long.parseLong(node.get("timestamp").asText())),
                            ZoneId.of("UTC")
                    ),
                    node.get("title").asText(),
                    node.get("comment").asText(),
                    node.get("user").asText(),
                    node.get("bot").asBoolean(),
                    node.get("wiki").asText(),
                    node.get("type").asText()
            );
            return Mono.just(input);
        } catch (NullPointerException e) {
            return Mono.error(e);
        }
    }
}
