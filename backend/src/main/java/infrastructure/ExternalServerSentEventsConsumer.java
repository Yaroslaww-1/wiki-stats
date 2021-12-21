package infrastructure;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class ExternalServerSentEventsConsumer {
    private final String baseUrl;
    private final String routeUrl;

    public ExternalServerSentEventsConsumer(String baseUrl, String routeUrl) {
        this.baseUrl = baseUrl;
        this.routeUrl = routeUrl;
    }

    public Flux<ServerSentEvent<String>> startConsuming() {
        var client = WebClient.create(baseUrl);
        var type = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        return client.get()
                .uri(routeUrl)
                .retrieve()
                .bodyToFlux(type);
    }
}
