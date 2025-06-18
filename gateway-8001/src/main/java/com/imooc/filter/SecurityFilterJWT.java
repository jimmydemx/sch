package com.imooc.filter;


import com.google.gson.Gson;
import com.imooc.base.BaseInfoProperties;
import com.imooc.configs.JWTExcludeUrl;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class SecurityFilterJWT extends BaseInfoProperties implements GlobalFilter, Ordered {

    @Autowired
    private JWTExcludeUrl jwtExcludeUrl;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private JWTUtils jwtUtils;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ;
        // 1. 或者当前的请求路径
        String path = exchange.getRequest().getURI().getPath();

        // 或者所有要陪排除的url list
        List<String> excludedUrls = jwtExcludeUrl.getUrls();

        // 校验
        boolean matched = excludedUrls.stream().anyMatch(excludedUrl -> antPathMatcher.matchStart(excludedUrl, path));

        if (matched) {
            return chain.filter(exchange);
        }

        // log
        log.info(path, " is excluded");

        // 不放行，如果找不到jwt，直接返回错误信息
        return VerifyJWT(exchange, chain);

    }


    private Mono<Void> VerifyJWT(ServerWebExchange exchange, GatewayFilterChain chain) {
        String userToken = exchange.getRequest().getHeaders().getFirst(USER_TOKEN);

        if (StringUtils.hasLength(userToken)) {
            String[] tokens = userToken.split(JWTUtils.at);
            if (tokens.length < 2) {
                return renderErrorMesg(exchange, ResponseStatusEnum.UN_LOGIN);
            }


            String prefix = tokens[0];
            String jwt = tokens[1];

            try {
                String s = jwtUtils.checkJWT(jwt);
                log.info("jwtstring is{}", s);
                
                setNewHeader(exchange, APP_USER_JSON, s);

                return chain.filter(exchange);
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                return renderErrorMesg(exchange, ResponseStatusEnum.JWT_EXPIRE_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
                return renderErrorMesg(exchange, ResponseStatusEnum.JWT_SIGNATURE_ERROR);
            }

        }
        return renderErrorMesg(exchange, ResponseStatusEnum.UN_LOGIN);

    }


    public Mono<Void> renderErrorMesg(ServerWebExchange exchange, ResponseStatusEnum statusEnum) {
        // 获得response
        ServerHttpResponse response = exchange.getResponse();

        // 构建json result
        GraceJSONResult jsonResult = GraceJSONResult.exception(statusEnum);

        // 修改response的code为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        // 设定header类型
        if (!response.getHeaders().containsKey("Content-Type")) {
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        }

        // 转化为json并向response中写从数据
        String json = new Gson().toJson(jsonResult);
        DataBuffer dataBuffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }


    public ServerWebExchange setNewHeader(ServerWebExchange exchange, String headerKey, String headerValue) {

        // 应为
        //exchange.getRequest().getHeaders().add(headerKey, headerValue);
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(headerKey, headerValue)
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

    // 过滤器的顺序，数字越小优先级越大
    @Override
    public int getOrder() {
        return 0;
    }
}
