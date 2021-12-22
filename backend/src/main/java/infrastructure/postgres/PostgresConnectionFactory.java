package infrastructure.postgres;

import infrastructure.configuration.DatabaseConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostgresConnectionFactory {
    public final R2dbcEntityTemplate template;
    private final PostgresqlConnectionFactory connectionFactory;

    @Autowired
    public PostgresConnectionFactory(DatabaseConfiguration configuration) {
        connectionFactory = new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration
                        .builder()
                        .host(configuration.host)
                        .port(configuration.port)
                        .database(configuration.database)
                        .username(configuration.username)
                        .password(configuration.password)
                        .build()
        );

        template = new R2dbcEntityTemplate(connectionFactory);
    }
}
