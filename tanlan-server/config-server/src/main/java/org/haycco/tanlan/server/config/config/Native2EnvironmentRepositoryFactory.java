package org.haycco.tanlan.server.config.config;

import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.EnvironmentRepositoryFactory;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * @author Dylan Roberts
 */
@Component
public class Native2EnvironmentRepositoryFactory implements EnvironmentRepositoryFactory<Native2EnvironmentRepository,
        NativeEnvironmentProperties> {
    private ConfigurableEnvironment environment;
    private ConfigServerProperties properties;

    public Native2EnvironmentRepositoryFactory(ConfigurableEnvironment environment, ConfigServerProperties properties) {
        this.environment = environment;
        this.properties = properties;
    }

    @Override
    public Native2EnvironmentRepository build(NativeEnvironmentProperties environmentProperties) {
        Native2EnvironmentRepository repository = new Native2EnvironmentRepository(environment, environmentProperties);
        if(properties.getDefaultLabel() != null) {
            repository.setDefaultLabel(properties.getDefaultLabel());
        }
        return repository;
    }
}
