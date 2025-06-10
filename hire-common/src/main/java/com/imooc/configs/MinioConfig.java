package com.imooc.configs;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private boolean secure;

    @PostConstruct
    public void validate() {
        Assert.notNull(endpoint, "MinIO endpoint must not be null");
        Assert.notNull(accessKey, "MinIO accessKey must not be null");
        Assert.notNull(secretKey, "MinIO secretKey must not be null");
    }
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
}
