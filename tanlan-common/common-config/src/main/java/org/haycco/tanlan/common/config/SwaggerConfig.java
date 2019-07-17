package org.haycco.tanlan.common.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * Swagger2 Reactive WebFlux Configuration
 *
 * @author haycco
 */
@ConditionalOnProperty(name="swagger.enable", havingValue="true")
@EnableConfigurationProperties(value = SwaggerProperties.class)
@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    @Autowired
    SwaggerProperties properties;

    @Value("${info.description}")
    private String description;

    @Value("${info.formatter-version}")
    private String formatterVersion;

    @Value("${info.build-timestamp}")
    private String buildTimestamp;

    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .groupName(properties.getDocket().getGroupName())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(properties.getApiInfo().getTitle())
                .description(description)
                .termsOfServiceUrl(properties.getApiInfo().getTermsOfServiceUrl())
                .contact(new Contact(properties.getApiInfo().getContact().getName(), properties.getApiInfo().getContact().getUrl(), properties.getApiInfo().getContact().getEmail()))
                .version(formatterVersion + " - " + buildTimestamp)
                .build();
    }

}
