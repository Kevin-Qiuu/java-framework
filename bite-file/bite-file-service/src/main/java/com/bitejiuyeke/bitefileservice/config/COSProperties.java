package com.bitejiuyeke.bitefileservice.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(value = "storage.type", havingValue = "cos")
@ConfigurationProperties(prefix = "cos")
public class COSProperties {

    /**
     * SId
     */
    private String secretId;
    /**
     * SKey
     */
    private String secretKey;
    /**
     * 上传文件超时时限（单位：秒）
     */
    private Long connectionTimeout;
    /**
     * 地域代码
     */
    private String regionName;
    /**
     * Bucket 名称
     */
    private String bucketName;
    /**
     * 文件存放路径前缀
     */
    private String pathPrefix;
    /**
     * 签名有效时间（单位：秒）
     */
    private Long signExpiredTime;


}
