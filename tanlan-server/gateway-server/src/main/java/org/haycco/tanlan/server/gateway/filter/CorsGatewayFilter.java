package org.haycco.tanlan.server.gateway.filter;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 网关跨域探测请求过滤设置
 *
 * @author haycco
 */
@Slf4j
@Component
@Order(-3)
public class CorsGatewayFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        String allowHeaders = StringUtils.join(exchange.getRequest().getHeaders().getAccessControlRequestHeaders(), ",");
        ServerHttpResponse response = exchange.getResponse();
        String allowMethodsStr = StringUtils.join(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()), ",");
        //允许所有跨域探测请求
        if(HttpMethod.OPTIONS.matches(exchange.getRequest().getMethodValue())) {
            log.debug("=======================Begin=======================");
            exchange.getRequest().getHeaders().forEach((k, v) -> {
                log.debug(k+": {}", StringUtils.join(v, ", "));
            });
            log.debug("=======================END=======================");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowMethodsStr);
            response.setStatusCode(HttpStatus.OK);

            return Mono.empty();
        }

        return chain.filter(exchange);
    }
}
