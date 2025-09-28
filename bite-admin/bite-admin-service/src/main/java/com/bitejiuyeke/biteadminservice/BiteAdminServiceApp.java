package com.bitejiuyeke.biteadminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bitejiuyeke.biteadminservice")
public class BiteAdminServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(BiteAdminServiceApp.class ,args);
    }
}
