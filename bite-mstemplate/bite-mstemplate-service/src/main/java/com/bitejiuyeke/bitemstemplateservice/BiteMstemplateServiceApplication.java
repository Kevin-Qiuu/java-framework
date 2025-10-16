package com.bitejiuyeke.bitemstemplateservice;

import com.bitejiuyeke.biteadminapi.config.feign.DictionaryFeignClient;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 网关服务的工作流程：
 * 1. 各个微服务启动时向 Nacos 注册自己的信息
 * 2. 网关服务连接到 Nacos，获取服务注册列表
 * 3. 当请求到达网关时，根据路由配置（如 lb://service-name）确定目标服务
 * 4. LoadBalancer 从 Nacos 获取的实例列表中选择一个实例进行转发
 * 5. 请求被转发到选定的微服务实例
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// todo 将扫描 feign 的配置放入 nacos 的配置中心
@EnableFeignClients(basePackages = {
        "com.bitejiuyeke.biteadminapi.map.feign",
        "com.bitejiuyeke.biteadminapi.config.feign"
})
public class BiteMstemplateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiteMstemplateServiceApplication.class, args);
    }

}
