package club.p6e.coat.permission;

import club.p6e.coat.permission.utils.SpringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class PermissionRunner implements ApplicationRunner {

    /**
     * 上下文对象
     */
    private final ApplicationContext application;

    /**
     * 构造方法初始化
     *
     * @param application 上下文对象
     */
    public PermissionRunner(ApplicationContext application) {
        this.application = application;
    }

    @Override
    public void run(ApplicationArguments args) {
        SpringUtil.init(application);
    }

}
