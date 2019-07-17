package org.haycco.tanlan.server.gateway.config;

import java.util.Arrays;
import java.util.List;
import jodd.util.collection.SortedArrayList;
import org.haycco.tanlan.common.config.SwaggerProperties;
import org.haycco.tanlan.server.gateway.util.RoutePathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * API 网关聚合Swagger2 在线文档
 *
 * @author haycco
 */
@Primary
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
@EnableConfigurationProperties(value = SwaggerProperties.class)
@Component
public class GatewaySwaggerConfig implements SwaggerResourcesProvider {

    @Autowired
    private Docket createRestApi;
    @Autowired
    private DiscoveryRouteDefinitionLocator discoveryClientRouter;

    private final List<String> ignoredModules = Arrays.asList("TANLAN-CONFIG-SERVER",//配置中心
                                                              "TANLAN-GATEWAY-SERVER",//对外网关
                                                              "TANLAN-BACKEND-GATEWAY-SERVER",//对内网关
                                                              "TANLAN-BACKEND-SERVICE",//后管平台
                                                              "TANLAN-MONITOR-SERVER",//监控服务
                                                              "TANLAN-HYSTRIX-DASHBOARD-SERVER");

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new SortedArrayList<>();
        discoveryClientRouter.getRouteDefinitions()
                .map(routeDefinition -> routeDefinition.getId().replace("ROUTE_", ""))
                .filter(serviceId -> !ignoredModules.contains(serviceId))
                .map(serviceId -> {
                    String path = RoutePathUtils.getBaseUri(serviceId);
                    return createResource(serviceId, path, createRestApi.getDocumentationType().getVersion());
                })
                .subscribe(resources::add);
        return resources;
    }

    private SwaggerResource createResource(String name, String url, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setUrl(url + "/v2/api-docs?group=" + createRestApi.getGroupName());
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}
