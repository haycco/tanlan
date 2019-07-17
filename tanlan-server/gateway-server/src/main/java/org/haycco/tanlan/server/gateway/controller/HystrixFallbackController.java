package org.haycco.tanlan.server.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.haycco.tanlan.common.ResponsePacket;
import org.haycco.tanlan.common.enums.BizExceptionEnum;
import org.haycco.tanlan.common.exception.HystrixFallbackException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 服务熔断回调
 *
 * @author haycco
 **/
@Slf4j
@RestController
@RequestMapping("/fallback")
public class HystrixFallbackController {

    @RequestMapping
    public Mono<ResponsePacket> fallback(ServerWebExchange exchange) {
        log.warn("RemoteAddress:{}", exchange.getRequest().getRemoteAddress().toString());
        log.warn("Request trigger fallback url is :{}", exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayOriginalRequestUrl").toString());
        Exception exception = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.hystrixExecutionException");
        log.warn("Method {} : {}", exchange.getRequest().getMethodValue(), exception);
        exchange.getRequest().getHeaders().forEach((key, value) -> log.warn(key + ":" + value));
        exchange.getRequest().getQueryParams().forEach((key, value) -> log.warn(key + ":" + value));

        return Mono.error(new HystrixFallbackException(BizExceptionEnum.HYSTRIX_SERVER, exception));
    }
}
