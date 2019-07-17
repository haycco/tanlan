package org.haycco.tanlan.server.gateway.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * 网关路由配置
 *
 * @author haycco
 **/
@Configuration
@AutoConfigureBefore(GatewayAutoConfiguration.class)
@AutoConfigureAfter(CompositeDiscoveryClientAutoConfiguration.class)
@ConditionalOnClass({DispatcherHandler.class, DiscoveryClient.class})
public class DiscoveryRouteConfig {

    /**
     * 注册一个自定义的路由定义加载器
     *
     * @param discoveryClient 服务发现
     */
    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
    public DiscoveryRouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient) {
        return new DiscoveryRouteDefinitionLocator(discoveryClient);
    }


}
