package org.haycco.tanlan.server.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                   .cors()
                   .and()
                   .formLogin().disable()
                   .httpBasic().disable()
                   .authenticationManager(authenticationManager)
                   .securityContextRepository(securityContextRepository)
                   .authorizeExchange()
                   .pathMatchers(HttpMethod.OPTIONS).permitAll()
                   .pathMatchers(HttpMethod.POST, "/backend/login","/login/phone", "/login/thirdpart", "/refresh_token").permitAll()
                   .pathMatchers("/qq/oauth/url","/wx/oauth/url", "/v1/qq/authorize", "/wx/sign").permitAll()
                   .anyExchange().authenticated()
                   .and()
                   .build();
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsRepository() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user")
//                .roles("USER", "ADMIN")
//                .build();
//        return new MapReactiveUserDetailsService(user);
//    }

   /* private AuthenticationWebFilter basicAuthenticationFilter(){
        UserDetailsRepositoryReactiveAuthenticationManager authManager;
        AuthenticationWebFilter basicAuthenticationFilter;
        ServerAuthenticationSuccessHandler successHandler;

        authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepository());
        successHandler = new  BasicAuthenticationSuccessHandler();

        basicAuthenticationFilter = new AuthenticationWebFilter(authManager);
        basicAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);

        return basicAuthenticationFilter;

    }

    private AuthenticationWebFilter bearerAuthenticationFilter(){
        AuthenticationWebFilter bearerAuthenticationFilter;
        Function<ServerWebExchange, Mono<Authentication>> bearerConverter;
        ReactiveAuthenticationManager authManager;

        authManager  = new BearerTokenReactiveAuthenticationManager();
        bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
        bearerConverter = new ServerHttpBearerAuthenticationConverter();

        bearerAuthenticationFilter.setAuthenticationConverter(bearerConverter);
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));

        return bearerAuthenticationFilter;
    }*/
}
