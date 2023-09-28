package club.p6e.coat.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 权限详情
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PermissionDetails implements Serializable {

    private Integer uid;
    private Integer gid;
    private String url;
    private String baseUrl;
    private String method;
    private Integer weight;
    private String mark;
    private String config;
    private String attribute;
    private String path;
    private Long version;

}
