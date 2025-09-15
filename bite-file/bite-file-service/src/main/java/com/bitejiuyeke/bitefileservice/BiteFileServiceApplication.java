package com.bitejiuyeke.bitefileservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
/**
 * 网关服务的工作流程：
 * 1. 各个微服务启动时向 Nacos 注册自己的信息
 * 2. 网关服务连接到 Nacos，获取服务注册列表
 * 3. 当请求到达网关时，根据路由配置（如 lb://service-name）确定目标服务
 * 4. LoadBalancer 从 Nacos 获取的实例列表中选择一个实例进行转发
 * 5. 请求被转发到选定的微服务实例
 *
 */
public class BiteFileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiteFileServiceApplication.class, args);
    }
}
