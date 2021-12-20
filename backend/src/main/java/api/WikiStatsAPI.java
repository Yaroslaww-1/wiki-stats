package api;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;

@SpringBootApplication
@Component
public class WikiStatsAPI  implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(WikiStatsAPI.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) throws Exception {

    }
}