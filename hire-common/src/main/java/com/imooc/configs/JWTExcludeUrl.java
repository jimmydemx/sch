package com.imooc.configs;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@PropertySource("classpath:jwt.properties")
@ConfigurationProperties(prefix = "exclude")
public class JWTExcludeUrl {
    private List<String> urls = new ArrayList<>();
}
