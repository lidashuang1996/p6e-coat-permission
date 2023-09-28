package club.p6e.coat.permission.demo.web;

import club.p6e.coat.permission.EnableP6ePermission;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableP6ePermission
@SpringBootApplication
public class P6eCoatPermissionWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(P6eCoatPermissionWebDemoApplication.class, args);
    }

}
