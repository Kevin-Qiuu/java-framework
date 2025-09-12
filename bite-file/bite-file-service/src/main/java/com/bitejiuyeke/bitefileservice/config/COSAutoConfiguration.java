package com.bitejiuyeke.bitefileservice.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "storage.type", havingValue = "cos")
public class COSAutoConfiguration {

    private COSClient cosClient;

    @Bean
    public COSClient cosClient(COSProperties cosProperties) {
        // 创建 COSClient
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(cosProperties.getRegionName());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        clientConfig.setConnectionTimeout(cosProperties.getConnectionTimeout());
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
        System.out.println("*************************");
        System.out.println(cosClient);
        return cosClient;
    }

    @PreDestroy
    public void closeCOSClient() {
        if (cosClient != null) {
            cosClient.shutdown();
        }
    }

}
