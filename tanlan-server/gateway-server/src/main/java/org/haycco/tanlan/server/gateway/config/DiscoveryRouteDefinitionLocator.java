package org.haycco.tanlan.server.gateway.config;

import static org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory.REGEXP_KEY;
import static org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory.REPLACEMENT_KEY;
import static org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory.PATTERN_KEY;
import static org.springframework.cloud.gateway.support.NameUtils.normalizeFilterFactoryName;
import static org.springframework.cloud.gateway.support.NameUtils.normalizeRoutePredicateName;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.server.gateway.util.RoutePathUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.HystrixGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

/**
 * 通过eureka发现服务定义路由
 *
 * @author haycco
 **/
@Slf4j
public class DiscoveryRouteDefinitionLocator implements RouteDefinitionLocator {

    private DiscoveryClient discoveryClient;

    public DiscoveryRouteDefinitionLocator(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(discoveryClient.getServices())
                .map(discoveryClient::getInstances)
                .filter(instances -> !instances.isEmpty())
                .map(instances -> {
                    String serviceId = instances.get(0).getServiceId();

                    String path = RoutePathUtils.getBaseUri(serviceId);

                    // rote
                    RouteDefinition routeDefinition = new RouteDefinition();
                    routeDefinition.setId("ROUTE_" + serviceId);
                    routeDefinition.setUri(URI.create("lb://" + serviceId));

                    // predicate
                    PredicateDefinition predicate = new PredicateDefinition();
                    predicate.setName(normalizeRoutePredicateName(PathRoutePredicateFactory.class));
                    predicate.addArg(PATTERN_KEY, path + "/**");
                    routeDefinition.getPredicates().add(predicate);

                    // 路径重写
                    FilterDefinition rewritePathFilter = new FilterDefinition();
                    rewritePathFilter.setName(normalizeFilterFactoryName(RewritePathGatewayFilterFactory.class));
                    rewritePathFilter.addArg(REGEXP_KEY, path + "/(?<remaining>.*)");
                    rewritePathFilter.addArg(REPLACEMENT_KEY, "/${remaining}");

                    // 重试 TODO 重试有问题，不设置
//                    FilterDefinition retryFilter = new FilterDefinition();
//                    retryFilter.setName(normalizeFilterFactoryName(RetryGatewayFilterFactory.class));
                    //重试次数-不包括本次（默认：3）
//                    retryFilter.addArg("retries", "2");
                    //重试的http status code 系列[1xx,2xx,3xx,4xx,5xx]（默认：5xx） @see org.springframework.http.HttpStatus.Series
//                    retryFilter.addArg("series", "SERVER_ERROR");
                    //重试的http status code（默认：无）@see org.springframework.http.HttpStatus
//                    retryFilter.addArg("statuses", "REQUEST_TIMEOUT");
                    //重试的请求方法（默认：GET）@see org.springframework.http.HttpMethod
//                    retryFilter.addArg("methods", "GET");
                    //重试的错误（默认：IOException.class, TimeoutException.class）
//                    retryFilter.addArg("exceptions", "IOException,TimeoutException");

                    /**
                     *  断路器
                     *  配置超时时间 @see hystrix.command.fallbackcmd.execution.isolation.thread.timeoutInMilliseconds:5000
                     */
                    FilterDefinition hystrixFilter = new FilterDefinition();
                    hystrixFilter.setName(normalizeFilterFactoryName(HystrixGatewayFilterFactory.class));
                    hystrixFilter.addArg("name", "fallbackcmd");
                    hystrixFilter.addArg("fallbackUri","forward:/fallback");

                    // 路径重写
                    routeDefinition.getFilters().add(rewritePathFilter);
                    // 重试
//                    routeDefinition.getFilters().add(retryFilter);
                    // 断路器
                    routeDefinition.getFilters().add(hystrixFilter);

                    // todo 白名单？

                    log.debug("refresh discovery route definition:{}", rewritePathFilter);

                    return routeDefinition;
                });
    }

}
