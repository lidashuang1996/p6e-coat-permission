package club.p6e.coat.permission;

/**
 * 权限任务接口
 *
 * @author lidashuang
 * @version 1.0
 */
public interface PermissionTask {

    /**
     * 最新版本号
     *
     * @return 版本号
     */
    public long version();

    /**
     * 间隔时间
     *
     * @return 间隔时间
     */
    public long interval();

}
