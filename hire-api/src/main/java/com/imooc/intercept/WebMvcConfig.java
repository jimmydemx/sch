package com.imooc.intercept;


import com.imooc.configs.JWTExcludeUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JWTCurrentUserInterceptor jwtCurrentUserInterceptor;

    @Autowired
    JWTExcludeUrl jwtExcludeUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtCurrentUserInterceptor)
                .addPathPatterns("/**").excludePathPatterns(String.valueOf(jwtExcludeUrl.getUrls()), "/static/**");


    }
}
