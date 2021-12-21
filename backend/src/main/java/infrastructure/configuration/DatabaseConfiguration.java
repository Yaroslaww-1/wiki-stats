package infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="database")
public class DatabaseConfiguration {
    public String host;
    public void setHost(String host) { this.host = host; }

    public String database;
    public void setDatabase(String database) { this.database = database; }

    public String username;
    public void setUsername(String username) { this.username = username; }

    public String password;
    public void setPassword(String password) { this.password = password; }
}
