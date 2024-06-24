package club.p6e.coat.permission;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 权限验证器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface PermissionValidator {

    /**
     * 验证是否具备权限
     *
     * @param path   请求的路径
     * @param method 请求的方法
     * @param groups 请求的用户权限组
     * @return Mono/PermissionDetails 通过权限的权限信息对象
     */
    public Mono<PermissionDetails> execute(String path, String method, List<String> groups);

    /**
     * 验证是否具备权限
     *
     * @param path    请求的路径
     * @param method  请求的方法
     * @param project 请求的用户项目
     * @param groups  请求的用户权限组
     * @return Mono/PermissionDetails 通过权限的权限信息对象
     */
    public Mono<PermissionDetails> execute(String path, String method, String project, List<String> groups);

}
