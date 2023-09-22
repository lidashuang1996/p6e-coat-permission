package club.p6e.coat.permission;

/**
 * 权限配置的接口
 *
 * @author lidashuang
 * @version 1.0
 */
public interface PermissionConfig {

    /**
     * 端口号
     *
     * @return 端口号
     */
    public int port();

    /**
     * 域名
     *
     * @return 域名
     */
    public String host();

    /**
     * 数据库名称
     *
     * @return 数据库名称
     */
    public String database();

    /**
     * 用户名称
     *
     * @return 用户名称
     */
    public String username();

    /**
     * 用户密码
     *
     * @return 用户密码
     */
    public String password();
}
