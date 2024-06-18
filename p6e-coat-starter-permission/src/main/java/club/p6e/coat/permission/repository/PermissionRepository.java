package club.p6e.coat.permission.repository;

import club.p6e.coat.common.utils.CopyUtil;
import club.p6e.coat.permission.model.PermissionModel;
import club.p6e.coat.permission.model.PermissionUrlGroupAssociationUrlModel;
import club.p6e.coat.permission.model.PermissionUrlGroupModel;
import club.p6e.coat.permission.model.PermissionUrlModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
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
public class PermissionRepository {

    /**
     * 模板对象
     */
    private final R2dbcEntityTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 模板对象
     */
    public PermissionRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Flux<PermissionModel> findPermission(Integer page, Integer size) {
        return findPermissionUrl(page, size)
                .flatMap(this::findPermissionUrlGroupAssociationUrl)
                .flatMap(this::findPermissionUrlGroup);
    }

    private Flux<PermissionModel> findPermissionUrl(Integer page, Integer size) {
        return template.select(
                Query.empty().sort(Sort.by(Sort.Order.asc(
                        PermissionUrlModel.ID))).offset((long) (page - 1) * size).limit(size),
                PermissionUrlModel.class
        ).map(m -> new PermissionModel()
                .setOid(m.getOid()).setPid(m.getPid())
                .setUid(m.getId()).setUUrl(m.getUrl())
                .setUBaseUrl(m.getBaseUrl()).setUMethod(m.getMethod())
        );
    }

    private Flux<PermissionModel> findPermissionUrlGroupAssociationUrl(PermissionModel pm) {
        return template.select(
                Query.query(Criteria
                        .where(PermissionUrlGroupAssociationUrlModel.UID).is(pm.getUid())
                        .and(PermissionUrlGroupAssociationUrlModel.OID).is(pm.getOid())
                        .and(PermissionUrlGroupAssociationUrlModel.PID).is(pm.getPid())
                ),
                PermissionUrlGroupAssociationUrlModel.class
        ).map(m -> CopyUtil.run(pm, PermissionModel.class).setGid(m.getGid()).setRConfig(m.getConfig()).setRAttribute(m.getAttribute()));
    }

    private Mono<PermissionModel> findPermissionUrlGroup(PermissionModel pm) {
        return template.selectOne(
                Query.query(Criteria
                        .where(PermissionUrlGroupModel.ID).is(pm.getGid())
                        .and(PermissionUrlGroupModel.OID).is(pm.getOid())
                        .and(PermissionUrlGroupModel.PID).is(pm.getPid())
                ),
                PermissionUrlGroupModel.class
        ).map(m -> pm.setGMark(m.getMark()).setGWeight(m.getWeight()));
    }

}
