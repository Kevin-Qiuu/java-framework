package com.bitejiuyeke.biteadminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.bitejiuyeke.biteadminservice")
@EnableFeignClients(basePackages = {"com.bitejiuyeke.**.feign"})
public class BiteAdminServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(BiteAdminServiceApp.class ,args);
    }
}
