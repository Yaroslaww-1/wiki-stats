package application.changes;

import application.admin.session.ISessionRepository;
import application.changes.addchange.AddChangeCommand;
import application.changes.addchange.AddChangeCommandHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.change.Change;
import infrastructure.ExternalServerSentEventsConsumer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class WikimediaServerSendEventsConsumer {
    private final Logger logger;
    private final AddChangeCommandHandler addChangeCommandHandler;
    private final ISessionRepository sessionRepository;

    @Autowired
    public WikimediaServerSendEventsConsumer(
            AddChangeCommandHandler addChangeCommandHandler,
            Logger logger,
            ISessionRepository sessionRepository
    ) {
        this.logger = logger;
        this.addChangeCommandHandler = addChangeCommandHandler;
        this.sessionRepository = sessionRepository;
    }

    public Flux<Change> startConsuming() {
        var url = "https://stream.wikimedia.org/v2/stream/recentchange";
        var consumer = new ExternalServerSentEventsConsumer(url, "/");
        return consumer.startConsuming()
                .onBackpressureDrop() //TODO: probably not the best solution
                .mapNotNull(ServerSentEvent::data)
                .concatMap(eventData -> this.executeAddChangeCommand(eventData)
                            .doOnError(throwable -> logger.error("Exception during parsing wikimedia event: " + throwable.getMessage()))
                            .onErrorResume(throwable -> Mono.empty())
                );
    }

    private Mono<Change> executeAddChangeCommand(String eventData) {
        final ObjectNode node;

        try {
            node = new ObjectMapper().readValue(eventData, ObjectNode.class);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }

        try {
            return Mono
                    .from(addChangeCommandHandler.execute(new AddChangeCommand(
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
                    )))
                    .delayElement(sessionRepository.getProcessingDelay());
        } catch (NullPointerException e) {
            return Mono.error(e);
        }
    }
}
