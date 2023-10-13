package club.p6e.coat.permission.config;

import club.p6e.coat.permission.PermissionConfig;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@Configuration
public class R2dbcConfig {

    @Bean("club.p6e.coat.permission.config.ConnectionFactory")
    @ConditionalOnMissingBean(
            value = ConnectionFactory.class,
            ignored = ConnectionFactory.class
    )
    public ConnectionFactory injectConnectionFactory(PermissionConfig config) {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration
                        .builder()
                        .host(config.host())
                        .port(config.port())
                        .database(config.database())
                        .username(config.username())
                        .password(config.password())
                        .build()
        );
    }

}
