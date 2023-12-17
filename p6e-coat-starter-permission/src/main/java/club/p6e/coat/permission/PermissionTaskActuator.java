package club.p6e.coat.permission;

import club.p6e.coat.common.utils.SpringUtil;
import club.p6e.coat.permission.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限任务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionTaskActuator.class,
        ignored = PermissionTaskActuator.class
)
public final class PermissionTaskActuator {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionTaskActuator.class);

    /**
     * 当前版本号
     */
    private long version = -1;

    /**
     * 权限任务对象
     */
    private final PermissionTask task;

    /**
     * 权限路径匹配器
     */
    private final PermissionPathMatcher matcher;

    /**
     * 构造方法初始化
     *
     * @param task          权限任务对象
     * @param matcher       权限路径匹配器
     * @param taskScheduler 定时任务对象
     */
    public PermissionTaskActuator(
            PermissionTask task,
            TaskScheduler taskScheduler,
            PermissionPathMatcher matcher
    ) {
        this.task = task;
        this.matcher = matcher;
        taskScheduler.scheduleWithFixedDelay(
                this::execute,
                Instant.now().plusSeconds(10),
                Duration.ofSeconds(task.interval())
        );
    }

    /**
     * 执行任务 （定时触发的任务）
     */
    public void execute() {
        final LocalDateTime now = LocalDateTime.now();
        LOGGER.info("[TASK] ==> now: " + now);
        LOGGER.info("[TASK] start executing permission update task.");
        final long currentVersion = task.version();
        if (version < currentVersion) {
            if (Boolean.TRUE.equals(execute0(currentVersion).block())) {
                version = currentVersion;
                matcher.deleteExpiredVersionData(version);
            }
        } else {
            LOGGER.info("[TASK] The version number is the " +
                    "latest version, and there is no need to perform data update operations.");
        }
        LOGGER.info("[TASK] complete the task of executing permission updates.");
    }

    /**
     * 执行任务
     *
     * @return 执行任务的结果
     */
    private Mono<Boolean> execute0(long version) {
        final int page = 1;
        final int size = 20;
        final List<PermissionDetails> list = new ArrayList<>();
        return execute1(page, size, list)
                .map(b -> {
                    if (b) {
                        LOGGER.info("[TASK] successfully read data, list data >>> [" + list.size() + "].");
                        list.forEach(item -> matcher.register(item.setVersion(version)));
                    }
                    return b;
                });
    }

    /**
     * 执行查询数据
     *
     * @param page 页码
     * @param size 页长
     * @param list 数据列表
     * @return 查询数据是否完成
     */
    private Mono<Boolean> execute1(int page, int size, List<PermissionDetails> list) {
        return execute2(page, size, list)
                .flatMap(s -> s == size ? execute1(page + 1, size, list) : Mono.just(true))
                .onErrorResume(e -> Mono.just(false));
    }

    /**
     * 执行查询数据
     *
     * @param page 页码
     * @param size 页长
     * @param list 数据列表
     * @return 查询数据的列表长度
     */
    private Mono<Integer> execute2(int page, int size, List<PermissionDetails> list) {
        LOGGER.info("[TASK] execute query data >>> page: "
                + page + ", size:  " + size + " ::: [" + list.size() + "].");
        final PermissionRepository repository = SpringUtil.getBean(PermissionRepository.class);
        return repository
                .findAll(page, size)
                .collectList()
                .map(l -> {
                    list.addAll(l.stream().map(item -> {
                        final PermissionDetails details = new PermissionDetails();
                        details.setUid(item.getUid());
                        details.setGid(item.getGid());
                        details.setUrl(item.getUUrl());
                        details.setBaseUrl(item.getUBaseUrl());
                        details.setMethod(item.getUMethod());
                        details.setMark(item.getGMark());
                        details.setWeight(item.getGWeight());
                        details.setConfig(item.getRConfig());
                        details.setAttribute(item.getRAttribute());
                        details.setPath(item.getUBaseUrl() + item.getUUrl());
                        return details;
                    }).toList());
                    return l.size();
                })
                .onErrorResume(e -> Mono.just(0));
    }

}
