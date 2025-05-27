package com.imooc.configs;

import feign.RequestInterceptor;
import io.seata.core.context.RootContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignSeataInterceptorConfiguration {

    @Bean
    public RequestInterceptor seataFeignInterceptor() {
        return requestTemplate -> {
            String xid = RootContext.getXID();
            if (xid != null) {
                requestTemplate.header(RootContext.KEY_XID, xid);
            }
        };
    }
}