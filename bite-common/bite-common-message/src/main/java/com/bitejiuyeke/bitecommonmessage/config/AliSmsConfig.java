package com.bitejiuyeke.bitecommonmessage.config;

import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dypnsapi20170525.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云通信短信认证服务配置
 */
@Configuration
@RefreshScope
public class AliSmsConfig {

    /**
     * 阿里云用户 accessKeyId
     */
    @Value("${sms.aliyun.accessKeyId}")
    private String accessKeyId;

    /**
     * 阿里云用户 accessKeySecret
     */
    @Value("${sms.aliyun.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 阿里云用户 endpoint
     */
    @Value("${sms.aliyun.endpoint:dypnsapi.aliyuncs.com}")
    private String endpoint;

    @Bean("smsAliyunClient")
    public Client client() throws Exception {
        Config config = new com.aliyun.teaopenapi.models.Config()
                // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endpoint);
        return new Client(config);
    }

}
