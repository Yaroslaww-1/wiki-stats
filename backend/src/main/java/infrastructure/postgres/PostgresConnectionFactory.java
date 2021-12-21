package infrastructure.postgres;

import infrastructure.configuration.DatabaseConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import name.nkonev.r2dbc.migrate.core.R2dbcMigrate;
import name.nkonev.r2dbc.migrate.core.R2dbcMigrateProperties;
import name.nkonev.r2dbc.migrate.reader.ReflectionsClasspathResourceReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

import java.util.Collections;

@Component
public class PostgresConnectionFactory {
    public final R2dbcEntityTemplate template;
    private final DatabaseConfiguration configuration;
    private final PostgresqlConnectionFactory connectionFactory;

    @Autowired
    public PostgresConnectionFactory(DatabaseConfiguration configuration) {
        this.configuration = configuration;
        connectionFactory = new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration
                        .builder()
                        .host(configuration.host)
                        .database(configuration.database)
                        .username(configuration.username)
                        .password(configuration.password)
                        .build()
        );

        template = new R2dbcEntityTemplate(connectionFactory);

        migrate();
    }

    private void migrate() {
        var connectionFactory = ConnectionFactories.get(
                ConnectionFactoryOptions
                        .builder()
                        .option(DRIVER, "postgresql")
                        .option(HOST, configuration.host.split(":")[0])
                        .option(PORT, Integer.parseInt(configuration.host.split(":")[1]))
                        .option(USER, configuration.username)
                        .option(PASSWORD, configuration.password)
                        .option(DATABASE, configuration.database)
                        .build()
        );

        R2dbcMigrateProperties properties = new R2dbcMigrateProperties();
        properties.setResourcesPaths(Collections.singletonList("classpath:../../../resources/db/migration/*.sql"));

        R2dbcMigrate.migrate(connectionFactory, properties, new ReflectionsClasspathResourceReader(), null).block();
    }
}
