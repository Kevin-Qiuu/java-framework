package com.bitejiuyeke.bitegateway;

import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,           // 排除数据库自动配置
        HibernateJpaAutoConfiguration.class,         // 排除 JPA 自动配置
})

public class BiteGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiteGatewayApplication.class, args);
        log.info("Java 脚手架网关服务已启动成功");
    }

}
