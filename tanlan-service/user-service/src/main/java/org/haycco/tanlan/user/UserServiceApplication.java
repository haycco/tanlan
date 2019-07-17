package org.haycco.tanlan.user;

import java.util.concurrent.Executors;

import org.haycco.tanlan.common.constant.CommonConstant;
import org.haycco.tanlan.common.event.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * 用户服务
 *
 * @author haycco
 */
@EnableReactiveFeignClients(basePackages = CommonConstant.SCAN_BASE_PACKAGES)
@SpringBootApplication(scanBasePackages = CommonConstant.SCAN_BASE_PACKAGES)
public class UserServiceApplication {

    public static void main(String[] args) {
        EventBus.create(Executors.newFixedThreadPool(5, r -> {
            Thread thread = new Thread(r);
            thread.setName("eventBus");
            thread.setDaemon(true);
            return thread;
        }));

        SpringApplication springApplication = new SpringApplication(UserServiceApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.run(args);
    }

}
