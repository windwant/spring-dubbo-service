package org.windwant.spring.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Administrator on 18-3-23.
 */
@Configuration
@EnableMongoRepositories({"org.windwant.spring.mongo"})
@PropertySource({"classpath:mongo.properties"})
public class MongoConfig {

    @Autowired
    protected Environment environment;

    @Bean
    public MongoClientFactoryBean mongoClientFactoryBean() {
        MongoClientFactoryBean factoryBean = new MongoClientFactoryBean();
        factoryBean.setHost(environment.getProperty("mongo.host"));
        factoryBean.setPort(Integer.parseInt(environment.getProperty("mongo.port")));
        try {
            factoryBean.afterPropertiesSet();
        } catch (Exception e) {
        }
        return factoryBean;
    }

    @Bean
    public Mongo mongoClient(MongoClientFactoryBean factoryBean) throws Exception {
        return factoryBean.getObject();
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient){
        return new MongoTemplate(mongoClient, environment.getProperty("mongo.database"));
    }
}
