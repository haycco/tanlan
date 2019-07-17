package org.haycco.tanlan.server.registry;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 微服务注册发现中心
 *
 * @author haycco
 */
@EnableEurekaServer
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RegistryServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RegistryServerApplication.class).run(args);
    }
}
