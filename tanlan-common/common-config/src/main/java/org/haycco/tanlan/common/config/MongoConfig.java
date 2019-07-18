package org.haycco.tanlan.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author haycco
 **/
@Configuration
@ConditionalOnProperty(prefix="spring.data.mongodb", name = "uri")
public class MongoConfig {

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Primary
    @Bean
    MongoTemplate mongoTemplate(MongoDbFactory dbFactory) {
        return new MongoTemplate(dbFactory);
    }
}
