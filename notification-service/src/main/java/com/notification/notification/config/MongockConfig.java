package com.notification.notification.config;

import com.mongodb.client.MongoClient;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.ChangeUnitFork;
import io.mongock.driver.api.driver.ChangeSetDependency;
import io.mongock.driver.mongodb.springdata.v3.driver.MongoSpringData3Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockSpringbootBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongockConfig {

    @Bean
    public MongockSpringbootBase mongockSpringboot(MongoTemplate mongoTemplate) {
        MongoSpringData3Driver driver = MongoSpringData3Driver.withDefaultLock(mongoTemplate);
        
        return MongockSpringboot.builder()
                .setDriver(driver)
                .addChangeLogsScanPackage("com.notification.notification.migrations")
                .build();
    }
}
