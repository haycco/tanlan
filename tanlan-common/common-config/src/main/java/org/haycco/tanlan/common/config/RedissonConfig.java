package org.haycco.tanlan.common.config;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haycco
 **/
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedissonReactiveClient redissonReactiveClient() {
        return Redisson.createReactive(this.getConfig());
    }

    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(this.getConfig());
    }

    private Config getConfig() {
        Config config = new Config();
        if (StringUtils.isNotEmpty(redisProperties.getUrl())) {
            config.useSingleServer()
                    .setAddress(redisProperties.getUrl())
                    .setPassword(redisProperties.getPassword())
                    .setDatabase(redisProperties.getDatabase());
        } else if (redisProperties.getCluster().getNodes().size() > 0) {
            config.useClusterServers()
                    .addNodeAddress(this.getClusterNodeAddresses().stream().findAny().get())
                    .setPassword(redisProperties.getPassword());
        }
        return config;
    }

    private List<String> getClusterNodeAddresses() {
        List<String> nodesAddress = new ArrayList<>(redisProperties.getCluster().getNodes().size());
        for (String node : redisProperties.getCluster().getNodes()) {
            nodesAddress.add("redis://" + node);
        }
        return nodesAddress;
    }
}
