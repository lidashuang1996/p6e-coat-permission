package club.p6e.coat.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证路径匹配器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionPathMatcher.class,
        ignored = PermissionPathMatcher.class
)
public class PermissionPathMatcher {

    /**
     * 注入日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionPathMatcher.class);

    /**
     * 路径模式解析器
     */
    private final PathPatternParser parser = new PathPatternParser();

    /**
     * 缓存需要拦截的路径匹配器
     */
    private final Map<PathPattern, List<PermissionDetails>> cache = new ConcurrentHashMap<>();

    /**
     * 匹配路径
     *
     * @param path 路径内容
     * @return 匹配请求的路径是否为拦截的路径地址
     */
    public List<PermissionDetails> match(String path) {
        final List<PermissionDetails> result = new ArrayList<>();
        final PathContainer container = PathContainer.parsePath(path);
        for (final PathPattern pattern : cache.keySet()) {
            if (pattern.matches(container)) {
                result.addAll(cache.get(pattern));
            }
        }
        result.sort(Comparator.comparingInt(PermissionDetails::getWeight).reversed());
        return result;
    }

    /**
     * 注册路径
     *
     * @param model 权限对象
     */
    public void register(PermissionDetails model) {
        if (model != null
                && model.getGid() != null
                && model.getUid() != null
                && model.getPath() != null
                && !model.getPath().isEmpty()) {
            final String path = model.getPath();
            for (final PathPattern pattern : cache.keySet()) {
                if (pattern.getPatternString().equalsIgnoreCase(path)) {
                    final String mark = model.getGid() + "_" + model.getUid();
                    final List<PermissionDetails> list = cache.get(pattern);
                    list.removeIf(item -> mark.equals(item.getGid() + "_" + item.getUid()));
                    LOGGER.info("[ REGISTER (ADD/REPLACE) ] " + path + " >>> " + model);
                    cache.get(parser.parse(path)).add(model);
                    return;
                }
            }
            LOGGER.info("[ REGISTER (ADD) ] " + path + "(" + model.getMethod() + ") >>> " + model);
            cache.put(parser.parse(path), new ArrayList<>(List.of(model)));
        }
    }

    /**
     * 卸载路径
     *
     * @param path 路径内容
     */
    public void unregister(PathPattern path) {
        cache.remove(path);
    }

    /**
     * 删除过期版本的权限数据
     *
     * @param version 版本号
     */
    public void deleteExpiredVersionData(long version) {
        for (final PathPattern key : cache.keySet()) {
            final List<PermissionDetails> list = cache.get(key);
            if (list != null && !list.isEmpty()) {
                list.removeIf(item -> item.getVersion() == null || item.getVersion() < version);
                if (list.isEmpty()) {
                    cache.remove(key);
                }
            }
        }
    }

}
