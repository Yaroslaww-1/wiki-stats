package application.edits;

import application.edits.addedit.AddEditCommand;
import application.edits.addedit.AddEditCommandHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.edit.Edit;
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
    private final AddEditCommandHandler addEditCommandHandler;

    @Autowired
    public WikimediaServerSendEventsConsumer(AddEditCommandHandler addEditCommandHandler, Logger logger) {
        this.logger = logger;
        this.addEditCommandHandler = addEditCommandHandler;
    }

    public Flux<Edit> startConsuming() {
        var url = "https://stream.wikimedia.org/v2/stream/recentchange";
        var consumer = new ExternalServerSentEventsConsumer(url, "/");
        return consumer.startConsuming()
                .onBackpressureDrop() //TODO: probably not the best solution
                .mapNotNull(ServerSentEvent::data)
                .concatMap(eventData -> this.executeAddEditCommand(eventData)
                            .doOnError(throwable -> logger.error("Exception during parsing wikimedia event: " + throwable.getMessage()))
                            .onErrorResume(throwable -> Mono.empty())
                );
    }

    private Mono<Edit> executeAddEditCommand(String eventData) {
        final ObjectNode node;

        try {
            node = new ObjectMapper().readValue(eventData, ObjectNode.class);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }

        try {
            return addEditCommandHandler.execute(new AddEditCommand(
                    node.get("id").asText(),
                    LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(Long.parseLong(node.get("timestamp").asText())),
                            ZoneId.of("UTC")
                    ),
                    node.get("title").asText(),
                    node.get("comment").asText(),
                    node.get("user").asText(),
                    node.get("bot").asBoolean(),
                    node.get("wiki").asText()
            ));
        } catch (NullPointerException e) {
            return Mono.error(e);
        }
    }
}
