package com.bitejiuyeke.biteadminservice.map.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MapConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 用于发送 Http 报文
        return new RestTemplate();
    }

}
