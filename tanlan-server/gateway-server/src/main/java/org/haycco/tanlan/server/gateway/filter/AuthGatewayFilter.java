package org.haycco.tanlan.server.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.haycco.tanlan.common.ResponsePacket;
import org.haycco.tanlan.common.constant.HttpHeadersConstant;
import org.haycco.tanlan.common.enums.BizExceptionEnum;
import org.haycco.tanlan.common.util.JacksonUtils;
import org.haycco.tanlan.server.gateway.config.FiltersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Component
@Slf4j
public class AuthGatewayFilter implements GlobalFilter, Ordered {

    @Resource
    private FiltersConfig filtersConfig;
    @Value("${auth.signing.key}")
    private String signingKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeadersConstant.HEADER_AUTH);

        if (StringUtils.isBlank(token)) {
            // 忽略不需要鉴权的路径
            if (filtersConfig.isIgnoreUrl(exchange)) {
                return chain.filter(exchange);
            }
            log.warn("token is blank");
            return unauthorized(exchange, BizExceptionEnum.INVALID_TOKEN);
        }

        if (filtersConfig.isForbiddenUrl(exchange)) {
            log.warn("this request method url is forbidden");
            return unauthorized(exchange, BizExceptionEnum.INVALID_TOKEN);
        }

        String uid;
        String accountType;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(signingKey.getBytes()))
                    .parseClaimsJws(token).getBody();
            uid = claims.get("id").toString();
            accountType = claims.get("accountType").toString();
        } catch (ExpiredJwtException e) {
            log.warn("token has expired [{}]", token);
            return unauthorized(exchange, BizExceptionEnum.EXPIRED_TOKEN);
        } catch (Exception e) {
            log.warn("illegal token [{}]", token);
            return unauthorized(exchange, BizExceptionEnum.INVALID_TOKEN);
        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    // 清除token
                    httpHeaders.remove(HttpHeadersConstant.HEADER_AUTH);
                    // 写入uid
                    httpHeaders.set(HttpHeadersConstant.HEADER_UID, uid);
                    //账号类型
                    httpHeaders.set(HttpHeadersConstant.HEADER_ACCOUNT, accountType);
                }).build();
        return chain.filter(exchange.mutate().request(request).build());

    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, BizExceptionEnum bizExceptionEnum) {
        ServerHttpResponse response = exchange.getResponse();
        String allowHeaders = StringUtils.join(exchange.getRequest().getHeaders().getAccessControlRequestHeaders(), ",");
        String allowMethodsStr = StringUtils.join(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(),
                HttpMethod.PUT.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()), ",");

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowMethodsStr);
        String jsonStr = JacksonUtils.toJsonWithSnake(ResponsePacket.onError(bizExceptionEnum));
        DataBuffer buffer = response.bufferFactory().wrap(jsonStr.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}