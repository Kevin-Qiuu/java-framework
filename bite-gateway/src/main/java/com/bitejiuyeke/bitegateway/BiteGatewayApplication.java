package com.bitejiuyeke.bitegateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})

public class BiteGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiteGatewayApplication.class, args);
        log.info("Java 脚手架网关服务已启动成功");
    }

}
