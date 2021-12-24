package api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MapperConfiguration {
    @Bean
    @Scope("prototype")
    public ObjectMapper mapper(final InjectionPoint ip) {
        return new ObjectMapper().findAndRegisterModules();
    }
}
