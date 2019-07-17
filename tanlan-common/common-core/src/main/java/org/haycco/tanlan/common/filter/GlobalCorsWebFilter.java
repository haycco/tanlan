package org.haycco.tanlan.common.filter;

import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 全局跨域设置（不含网关层聚合）
 *
 * @author haycco
 */
@Configuration
@ConditionalOnProperty(value = "web.filter.cors.enabled", matchIfMissing = true)
public class GlobalCorsWebFilter {

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL));
        corsConfig.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        corsConfig.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
