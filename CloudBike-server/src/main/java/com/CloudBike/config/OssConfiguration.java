package com.CloudBike.config;

import com.CloudBike.properties.AliOssProperties;
import com.CloudBike.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties)
    {
        log.info("创建阿里云OSS文件上传工具类对象:{}",aliOssProperties);
        return AliOssUtil.builder()
                .endpoint(aliOssProperties.getEndpoint())
                .accessKeyId(aliOssProperties.getAccessKeyId())
                .accessKeySecret(aliOssProperties.getAccessKeySecret())
                .bucketName(aliOssProperties.getBucketName())
                .build();
    }
}