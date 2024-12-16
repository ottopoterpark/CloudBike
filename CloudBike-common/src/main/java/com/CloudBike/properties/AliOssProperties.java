package com.CloudBike.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云存储信息配置类
 * @author unique
 */
@Component
@ConfigurationProperties(prefix = "cloudbike.alioss")
@Data
public class AliOssProperties {

    /**
     * 访问域名
     */
    private String endpoint;
    /**
     * id
     */
    private String accessKeyId;
    /**
     * 密钥
     */
    private String accessKeySecret;
    /**
     * 存储空间
     */
    private String bucketName;

}
