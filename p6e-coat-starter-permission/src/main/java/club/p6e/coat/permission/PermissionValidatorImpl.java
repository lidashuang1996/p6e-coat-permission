package club.p6e.coat.permission;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 权限验证器默认的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionValidator.class,
        ignored = PermissionValidatorImpl.class
)
public class PermissionValidatorImpl implements PermissionValidator {

    /**
     * 权限路径匹配器
     */
    private final PermissionPathMatcher matcher;

    /**
     * 构造方法初始化
     *
     * @param matcher 权限路径匹配器
     */
    public PermissionValidatorImpl(PermissionPathMatcher matcher) {
        this.matcher = matcher;
    }

    /**
     * 验证是否具备权限
     *
     * @param path   请求的路径
     * @param method 请求的方法
     * @param groups 请求的用户权限组
     * @return Mono/PermissionDetails 通过权限的权限信息对象
     */
    @Override
    public Mono<PermissionDetails> execute(String path, String method, List<String> groups) {
        System.out.println(path);
        System.out.println(method);
        System.out.println(groups);
        if (groups != null) {
            final List<PermissionDetails> permissions = matcher.match(path);
            System.out.println("permissions >> " + permissions);
            if (permissions != null && !permissions.isEmpty()) {
                for (final PermissionDetails permission : permissions) {
                    final String pm = permission.getMethod();
                    final String pg = String.valueOf(permission.getGid());
                    if (groups.contains(pg) && ("*".equals(pm) || method.equalsIgnoreCase(pm))) {
                        return Mono.just(permission);
                    }
                }
            }
        }
        return Mono.empty();
    }

}
