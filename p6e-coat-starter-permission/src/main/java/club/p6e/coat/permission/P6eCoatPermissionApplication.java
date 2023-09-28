package club.p6e.coat.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableP6ePermission
@SpringBootApplication
public class P6eCoatPermissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eCoatPermissionApplication.class, args);
    }

}
