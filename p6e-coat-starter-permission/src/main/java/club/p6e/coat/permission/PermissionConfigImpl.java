package club.p6e.coat.permission;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionConfig.class,
        ignored = PermissionConfigImpl.class
)
public class PermissionConfigImpl implements PermissionConfig {

    @Value("${spring.r2dbc.url:#{null}}")
    private String r2dbcUrl;

    @Value("${spring.r2dbc.username:#{null}}")
    private String r2dbcUsername;

    @Value("${spring.r2dbc.password:#{null}}")
    private String r2dbcPassword;

    @Value("${spring.datasource.url:#{null}}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:#{null}}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:#{null}}")
    private String datasourcePassword;

    @Override
    public int port() {
        final String url = r2dbcUrl == null ? (datasourceUrl == null
                ? "jdbc:postgresql://localhost:5432/p6e" : datasourceUrl) : r2dbcUrl;
        final String[] content = url.split("//");
        final String hp = content[1].substring(0, content[1].indexOf("/"));
        final int index = hp.indexOf(":");
        final String port = index < 0 ? "5432" : hp.substring(index + 1);
        return Integer.parseInt(port);
    }

    @Override
    public String host() {
        final String url = r2dbcUrl != null ? r2dbcUrl : (
                datasourceUrl == null ? "jdbc:postgresql://localhost:5432/p6e" : datasourceUrl);
        final String[] content = url.split("//");
        final String hp = content[1].substring(0, content[1].indexOf("/"));
        final int index = hp.indexOf(":");
        return hp.substring(0, index < 0 ? hp.length() : index);
    }

    @Override
    public String database() {
        final String url = r2dbcUrl == null ? (datasourceUrl == null
                ? "jdbc:postgresql://localhost:5432/p6e" : datasourceUrl) : r2dbcUrl;
        final int index = url.lastIndexOf("?");
        final String content = url.substring(0, index < 0 ? url.length() : index);
        return content.substring(content.lastIndexOf("/") + 1);
    }

    @Override
    public String username() {
        return r2dbcUsername == null ? (datasourceUsername == null ? "postgres" : datasourceUsername) : r2dbcUsername;
    }

    @Override
    public String password() {
        return r2dbcPassword == null ? (datasourcePassword == null ? "postgres" : datasourcePassword) : r2dbcPassword;
    }

}
