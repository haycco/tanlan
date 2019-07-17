package org.haycco.tanlan.order;

import org.haycco.tanlan.common.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * Order Service
 *
 * @author haycco
 */
@EnableReactiveFeignClients(basePackages = CommonConstant.SCAN_BASE_PACKAGES)
@SpringBootApplication(scanBasePackages = CommonConstant.SCAN_BASE_PACKAGES)
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(OrderServiceApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.run(args);
    }

}
