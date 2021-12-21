package api;

import api.configuration.LoggerConfiguration;
import application.edits.WikimediaServerSendEventsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = { "application", "domain", "infrastructure", "api" })
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties
public class WikiStatsAPI implements ApplicationRunner {
    @Autowired
    private WikimediaServerSendEventsConsumer wikimediaServerSendEventsConsumer;

    @Autowired
    private LoggerConfiguration loggerConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(WikiStatsAPI.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) {
//        wikimediaServerSendEventsConsumer.startConsuming()
//                .subscribe();
    }
}