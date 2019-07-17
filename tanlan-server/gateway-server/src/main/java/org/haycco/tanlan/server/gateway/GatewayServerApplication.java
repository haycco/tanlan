package org.haycco.tanlan.server.gateway;

import org.haycco.tanlan.common.config.RedisConfig;
import org.haycco.tanlan.common.config.RedissonConfig;
import org.haycco.tanlan.common.constant.CommonConstant;
import org.haycco.tanlan.common.lock.RLockAspect;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 微服务对外统一访问网关服务
 *
 * @author haycco
 */
@EnableDiscoveryClient
@EnableCircuitBreaker
@RefreshScope
@ComponentScan(basePackages = {CommonConstant.SCAN_BASE_PACKAGES}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {RedisConfig.class, RedissonConfig.class, RLockAspect.class})})
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, MongoAutoConfiguration.class},
        scanBasePackages = CommonConstant.SCAN_BASE_PACKAGES)
public class GatewayServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayServerApplication.class).run(args);
    }
}

