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

    @Value("#{'${spring.r2dbc.url}'}")
    private String url;

    @Value("#{'${spring.r2dbc.username}'}")
    private String username;

    @Value("#{'${spring.r2dbc.password}'}")
    private String password;

    @Override
    public int port() {
        final String[] content = url.split("//");
        final String hp = content[1].substring(0, content[1].indexOf("/"));
        final int index = hp.indexOf(":");
        final String port = index < 0 ? "5432" : hp.substring(index + 1);
        return Integer.parseInt(port);
    }

    @Override
    public String host() {
        final String[] content = url.split("//");
        final String hp = content[1].substring(0, content[1].indexOf("/"));
        final int index = hp.indexOf(":");
        return hp.substring(0, index < 0 ? hp.length() : index);
    }

    @Override
    public String database() {
        final int index = url.lastIndexOf("?");
        final String content = url.substring(0, index < 0 ? url.length() : index);
        return content.substring(content.lastIndexOf("/") + 1);
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

}
