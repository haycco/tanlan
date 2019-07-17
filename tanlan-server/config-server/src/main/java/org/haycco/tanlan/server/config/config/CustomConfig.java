package org.haycco.tanlan.server.config.config;

import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 修复BUG：https://github.com/spring-cloud/spring-cloud-config/issues/1254
 *
 * @author haycco
 */
@Configuration
public class CustomConfig {

    @Bean
    public Native2EnvironmentRepository nativeEnvironmentRepository(Native2EnvironmentRepositoryFactory factory,
            NativeEnvironmentProperties environmentProperties) {
        return factory.build(environmentProperties);
    }

}
