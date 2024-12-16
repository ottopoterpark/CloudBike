package com.CloudBike.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序属性配置类
 * @author unique
 */
@Component
@ConfigurationProperties(prefix = "cloudbike.wechat")
@Data
public class WeChatProperties {
    /**
     * 小程序appid
     */
    private String appid;
    /**
     * 小程序的密钥
     */
    private String secret;
}
