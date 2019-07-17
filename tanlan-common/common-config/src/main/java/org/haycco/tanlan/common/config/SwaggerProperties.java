package org.haycco.tanlan.common.config;

import org.haycco.tanlan.common.config.properties.ApiInfoProperties;
import org.haycco.tanlan.common.config.properties.DocketProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger Properties
 *
 * @author haycco
 */
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private Boolean enable = false;
    private ApiInfoProperties apiInfo;
    private DocketProperties docket;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public ApiInfoProperties getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfoProperties apiInfo) {
        this.apiInfo = apiInfo;
    }

    public DocketProperties getDocket() {
        return docket;
    }

    public void setDocket(DocketProperties docket) {
        this.docket = docket;
    }
}
