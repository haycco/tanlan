package org.haycco.tanlan.server.gateway.config;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

/**
 * 网关过滤器配置
 *
 * @author haycco
 **/
@Slf4j
@RefreshScope
@Configuration
@EnableConfigurationProperties({FiltersConfigProperties.class})
public class FiltersConfig {

    @Autowired
    private FiltersConfigProperties filterProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public FiltersConfigProperties getFilterProperties(){
        return filterProperties;
    }

    public boolean isIgnoreUrl(ServerWebExchange exchange) {
        return this.isContainUrl(filterProperties.getIgnoreUrls(), exchange);
    }

    public boolean isForbiddenUrl(ServerWebExchange exchange) {
        return this.isContainUrl(filterProperties.getForbiddenUrls(), exchange);
    }

    private boolean isContainUrl(List<String> apiPaths, ServerWebExchange exchange) {

        return apiPaths.parallelStream().anyMatch(apiPath -> {

            String requestUrl = exchange.getRequest().getPath().toString();
            String requestMethod = exchange.getRequest().getMethodValue();

            String method = null;
            if (apiPath.contains(" ")) {
                String[] m = apiPath.split("\\s+");
                method = m[0];
                apiPath = m[1];
            }

            boolean pathMatched = antPathMatcher.match(apiPath, requestUrl);

            if (method == null) {
                log.debug("url matched: [{}]->[{}] [{}]", apiPath, method, requestUrl);
                return pathMatched;

            }
            log.debug("url matched: [{}]->[{}] [{}]", apiPath, method, requestUrl);

            return pathMatched && method.contains(requestMethod);
        });
    }

}
