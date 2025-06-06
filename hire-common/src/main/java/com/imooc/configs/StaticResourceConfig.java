package com.imooc.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {


    /**
     * 添加静态资源的路径，css/js/html等都可以放在其中进行虚拟化
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").
                addResourceLocations("file:D:/face/");
//                addResourceLocations("classpath:/static/");

//        super.addResourceHandlers(registry);
    }
}
