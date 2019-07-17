package org.haycco.tanlan.server.gateway.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 全局过滤器参数
 *
 * @author haycco
 **/
@ConfigurationProperties(prefix = "filter")
@RefreshScope
public class FiltersConfigProperties {

    private List<String> ignoreUrls = new ArrayList<>();

    private List<String> forbiddenUrls = new ArrayList<>();

    private Staging staging;

    public List<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(List<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public List<String> getForbiddenUrls() {
        return forbiddenUrls;
    }

    public void setForbiddenUrls(List<String> forbiddenUrls) {
        this.forbiddenUrls = forbiddenUrls;
    }

    public Staging getStaging() {
        return staging;
    }

    public void setStaging(Staging staging) {
        this.staging = staging;
    }

    @Data
    @NoArgsConstructor
    public static class Staging {

        Boolean enabled = false;
        String whitelist;
        String version;

    }
}
