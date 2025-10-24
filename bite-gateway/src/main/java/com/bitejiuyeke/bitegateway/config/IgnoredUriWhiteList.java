package com.bitejiuyeke.bitegateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
@RefreshScope // 当配置中心推送配置变更时,标注 @RefreshScope 的 Bean 会被重新创建,相关实例会重新绑定最新的配置值
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoredUriWhiteList {

    /**
     * 网关 token 校验放行白名单
     */
    private List<String> whiteList = new ArrayList<>();

}
