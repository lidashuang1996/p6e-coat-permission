package club.p6e.coat.permission;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfigureImportSelector.class)
public @interface EnableP6ePermission {
}
