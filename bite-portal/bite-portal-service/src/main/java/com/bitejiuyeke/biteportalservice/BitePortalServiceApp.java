package com.bitejiuyeke.biteportalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.bitejiuyeke.**.feign"})
public class BitePortalServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(BitePortalServiceApp.class, args);
    }

}
