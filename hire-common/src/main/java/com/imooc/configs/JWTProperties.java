package com.imooc.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "auth")
@PropertySource("classpath:jwt.properties")
public class JWTProperties {
    private String key;
}
