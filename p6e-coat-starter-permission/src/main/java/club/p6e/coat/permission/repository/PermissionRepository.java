package club.p6e.coat.permission.repository;

import club.p6e.coat.permission.model.PermissionModel;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 权限存储库
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionRepository.class,
        ignored = PermissionRepository.class
)
public class PermissionRepository {

    /**
     * SQL
     */
    @SuppressWarnings("ALL")
    private static final String SQL = "" +
            "SELECT   " +
            "   \"TU\".\"id\" AS u_id,  " +
            "   \"TU\".\"url\" AS u_url,   " +
            "   \"TU\".\"base_url\" AS u_base_url,   " +
            "   \"TU\".\"method\" AS u_method,   " +
            "   \"TU\".\"name\" AS u_name,   " +
            "   \"TU\".\"config\" AS u_config,   " +
            "   \"TG\".\"id\" AS g_id,   " +
            "   \"TG\".\"weight\" AS g_weight,   " +
            "   \"TG\".\"mark\" AS g_mark,   " +
            "   \"TG\".\"name\" AS g_name,   " +
            "   \"TR\".\"config\" AS r_config,   " +
            "   \"TR\".\"attribute\" AS r_attribute    " +
            "FROM   " +
            "   (   " +
            "       SELECT   " +
            "           \"id\",   " +
            "           \"url\",   " +
            "           \"base_url\",   " +
            "           \"method\",   " +
            "           \"name\",   " +
            "           \"config\"   " +
            "       FROM   " +
            "           \"p6e_permission_url\"    " +
            "       ORDER BY \"id\" ASC" +
            "       LIMIT :limit OFFSET :offset   " +
            "   ) AS \"TU\"   " +
            "   LEFT JOIN   " +
            "       \"p6e_permission_url_group_relation_url\" AS \"TR\"   " +
            "   ON   " +
            "       \"TU\".\"id\" = \"TR\".\"uid\"   " +
            "   LEFT JOIN   " +
            "       \"p6e_permission_url_group\" AS \"TG\"   " +
            "   ON   " +
            "       \"TR\".\"gid\" = \"TG\".\"id\"   " +
            ";";

    /**
     * 连接工厂
     */
    private final ConnectionFactory factory;

    /**
     * 构造方法初始化
     *
     * @param factory 连接工厂
     */
    public PermissionRepository(ConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * 查询权限 URL 表格对应的全部数据
     *
     * @param page 分页页码
     * @param size 分页长度
     * @return Flux/PermissionModel 查询数据结果
     */
    public Flux<PermissionModel> findAll(Integer page, Integer size) {
        page = page == null ? 1 : (page <= 0 ? 1 : page);
        size = size == null ? 16 : (size <= 0 ? 16 : (size > 200 ? 200 : size));
        final String sql = SQL
                .replace(":limit", String.valueOf(size))
                .replace(":offset", String.valueOf((page - 1) * size));
        return Flux
                .from(this.factory.create())
                .flatMap(connection -> Mono.from(connection.createStatement(sql).execute()))
                .flatMap(result -> Flux.from(
                        result.map((row, metadata) -> {
                            final PermissionModel model = new PermissionModel();
                            model.setUid(row.get("u_id", Integer.class));
                            model.setUUrl(row.get("u_url", String.class));
                            model.setUBaseUrl(row.get("u_base_url", String.class));
                            model.setUMethod(row.get("u_method", String.class));
                            model.setUName(row.get("u_name", String.class));
                            model.setUConfig(row.get("u_config", String.class));
                            model.setGid(row.get("g_id", Integer.class));
                            model.setGWeight(row.get("g_weight", Integer.class));
                            model.setGMark(row.get("g_mark", String.class));
                            model.setGName(row.get("g_name", String.class));
                            model.setRConfig(row.get("r_config", String.class));
                            model.setRAttribute(row.get("r_attribute", String.class));
                            return model;
                        }))
                );
    }


}
