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

import java.time.LocalDateTime;

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
                .mapNotNull(ServerSentEvent::data)
                .concatMap(this::mapEventDataToAddEditCommand)
                .doOnError(throwable -> logger.error("Exception during parsing wikimedia event", throwable))
                .concatMap(addEditCommandHandler::execute);
    }

    private Mono<AddEditCommand> mapEventDataToAddEditCommand(String eventData) {
        try {
            final ObjectNode node = new ObjectMapper().readValue(eventData, ObjectNode.class);

            return Mono.just(
                    new AddEditCommand(
                            node.get("id").asText(),
                            LocalDateTime.parse(node.get("timestamp").asText()),
                            node.get("title").asText(),
                            node.get("comment").asText(),
                            node.get("editor").asText(),
                            node.get("bot").asBoolean(),
                            node.get("wiki").asText()
                    )
            );
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
