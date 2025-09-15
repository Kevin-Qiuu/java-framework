package com.bitejiuyeke.bitefileservice.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(value = "storage.type", havingValue = "oss")
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {

    /**
     * 默认外网 endpoint
     */
    private String endpoint;
    /**
     * ak
     */
    private String accessKeyId;
    /**
     * as
     */
    private String accessKeySecret;
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 地域代码
     */
    private String regionName;
    /**
     * 存储 key 前缀
     */
    private String pathPrefix;
    /**
     * 签名过期时间（单位：秒）
     */
    private Long signExpiredTime;
    /**
     * 最小上传长度（单位；字节）
     */
    private Long minLen;
    /**
     * 最大上传长度（单位：字节）
     */
    private Long maxLen;

    public String getBaseUrl() {
        return "https://" + bucketName + "." + endpoint + "/";
    }

}
