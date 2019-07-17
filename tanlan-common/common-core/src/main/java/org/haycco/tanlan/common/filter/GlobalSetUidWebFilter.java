package org.haycco.tanlan.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.haycco.tanlan.common.UserContext;
import org.haycco.tanlan.common.constant.HttpHeadersConstant;
import org.haycco.tanlan.common.domain.IgnorePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 设置全局请求用户uid过滤器
 *
 * @author haycco
 */
@Slf4j
@Component
@Order(0)
@ConditionalOnProperty(value = "web.filter.set-uid.enable", matchIfMissing = true)
public class GlobalSetUidWebFilter implements WebFilter {

    private List<IgnorePath> ignorePaths;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public GlobalSetUidWebFilter(@Value("${web.filter.set-uid.ignoreUrls}") List<String> ignoreUrls) {
        ignorePaths = new ArrayList<>(ignoreUrls.size());
        ignoreUrls.stream().forEach(path -> {
            String methods = null;
            if (path.contains(" ")) {
                String[] m = StringUtils.split(path,"\\s+");
                methods = m[0];
                path = m[1];
            }
            ignorePaths.add(IgnorePath.builder().path(path).methods(methods).build());
        });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String deviceId = exchange.getRequest().getHeaders().getFirst(HttpHeadersConstant.HEADER_DEVICE_ID);
        String userId = exchange.getRequest().getHeaders().getFirst(HttpHeadersConstant.HEADER_UID);
        String accountTypeStr = exchange.getRequest().getHeaders().getFirst(HttpHeadersConstant.HEADER_ACCOUNT);
        Integer accountType = Optional.ofNullable(accountTypeStr).map(Integer::valueOf).orElse(null);

        boolean ignoreUrl = isIgnoreUrl(exchange);
        if (ignoreUrl) {
            log.trace("path = {}  >>> set deviceId:{}", exchange.getRequest().getPath(), deviceId);
            UserContext.setUser(deviceId, null, null);
        } else {
            log.trace("path = {}  >>> set userId:{} accountType:{}", exchange.getRequest().getPath(), userId, accountType);
            UserContext.setUser(deviceId, userId, accountType);
        }

        return chain.filter(exchange)
                .subscriberContext(ctx -> {
                    if (!StringUtils.isEmpty(userId)) {
                        ctx.put(UserContext.USER_ID_KEY, userId);
                    }
                    if (Objects.nonNull(accountType)) {
                        ctx.put(UserContext.ACCOUNT_TYPE_KEY, accountType);
                    }
                    if (!StringUtils.isEmpty(deviceId)) {
                        ctx.put(UserContext.DEVICE_ID_KEY, deviceId);
                    }
                    return ctx;
                })
                .doFinally(Void -> {
                    if (!ignoreUrl) {
                        UserContext.clean();
                    }
                });
    }

    protected boolean isIgnoreUrl(ServerWebExchange exchange) {
        return ignorePaths.parallelStream().anyMatch(ignorePath -> {

            String resUrl = exchange.getRequest().getPath().toString();

            String method = exchange.getRequest().getMethodValue();

            boolean pathMatched = antPathMatcher.match(ignorePath.getPath(), resUrl);

            if (pathMatched) {
                if (ignorePath.getMethods() == null) {
                    log.trace("web.filter.set-uid ignore url matched: [{}]->[{}] [{}]", ignorePath, method, resUrl);
                    return true;

                }
                if (ignorePath.getMethods().contains(method)) {
                    log.trace("web.filter.set-uid ignore url matched: [{}]->[{}] [{}]", ignorePath, method, resUrl);
                    return true;
                }
            }

            return false;
        });
    }

}