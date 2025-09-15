package com.bitejiuyeke.bitefileservice.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "storage.type", havingValue = "oss")
public class OSSAutoConfiguration {

    @Autowired
    private OSSProperties ossProperties;

    private OSS ossClient;

    @Bean
    public OSS ossClient() {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        DefaultCredentialProvider credentialsProvider =
                CredentialsProviderFactory.newDefaultCredentialProvider(accessKeyId, accessKeySecret);
        String region = ossProperties.getRegionName();

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
        .endpoint(endpoint)
        .credentialsProvider(credentialsProvider)
        .clientConfiguration(clientBuilderConfiguration)
        .region(region)
        .build();
        this.ossClient = ossClient;
        return ossClient;
    }

    @PreDestroy
    public void closeClient() {
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }

}
