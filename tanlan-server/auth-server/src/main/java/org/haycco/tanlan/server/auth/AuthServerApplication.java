package org.haycco.tanlan.server.auth;

import org.haycco.tanlan.common.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * 鉴权服务
 *
 * @author haycco
 */
@EnableReactiveFeignClients(basePackages = CommonConstant.SCAN_BASE_PACKAGES)
@SpringBootApplication(scanBasePackages = CommonConstant.SCAN_BASE_PACKAGES, exclude = MongoAutoConfiguration.class)
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AuthServerApplication.class);
        application.setAllowBeanDefinitionOverriding(true);
        application.run(args);
    }

}
