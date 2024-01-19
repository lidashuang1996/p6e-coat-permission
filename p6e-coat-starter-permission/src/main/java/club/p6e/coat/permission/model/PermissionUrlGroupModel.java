package club.p6e.coat.permission.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限组模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PermissionUrlGroupModel implements Serializable {

    public static final String TABLE = "p6e_permission_url_group";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String MARK = "mark";
    public static final String WEIGHT = "weight";
    public static final String PARENT = "parent";
    public static final String DESCRIPTION = "description";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";

    private Integer id;
    private String name;
    private String mark;
    private Integer weight;
    private Integer parent;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer operator;
    private Integer version;

}
