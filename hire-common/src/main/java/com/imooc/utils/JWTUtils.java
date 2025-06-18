//	此资源由 58学课资源站 收集整理
//	想要获取完整课件资料 请访问：58xueke.com
//	百万资源 畅享学习
package com.imooc.utils;

import com.imooc.configs.JWTProperties;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
@RefreshScope
public class JWTUtils {

    public static final String at = "@";

    @Autowired
    private JWTProperties jwtProperties;

    @Value("${auth.key}")
    public String JWT_KEY;

    @Value("${jwt.key}")
    public String JWT_SECRET;

    public String createJWTWithPrefix(String body, Long expireTimes, String prefix) {
        if (expireTimes == null)
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        return prefix + at + createJWT(body, expireTimes);
    }

    public String createJWTWithPrefix(String body, String prefix) {
        return prefix + at + createJWT(body);
    }

    public String createJWT(String body) {
        return dealJWT(body, null);
    }

    public String createJWT(String body, Long expireTimes) {
        if (expireTimes == null)
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        return dealJWT(body, expireTimes);
    }

    public String dealJWT(String body, Long expireTimes) {

//        String userKey = jwtProperties.getKey();
        String userKey = JWT_KEY;
        log.info("Nacos jwt key = " + JWT_KEY);
        log.info("key store in nacos:{}", JWT_SECRET);

        // 1. 对秘钥进行base64编码

        String base64 = Base64.getEncoder().encodeToString(userKey.getBytes());

        // 2. 对base64生成一个秘钥的对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());

        String jwt = "";
        if (expireTimes != null) {
            jwt = generatorJWT(body, expireTimes, secretKey);
        } else {
            jwt = generatorJWT(body, secretKey);
        }
        log.info("JWTUtils - dealJWT: generatorJWT = " + jwt);

        return jwt;
    }

    public String generatorJWT(String body, SecretKey secretKey) {
        return Jwts.builder()
                .subject(body)
                .signWith(secretKey)
                .compact();
    }

    public String generatorJWT(String body, Long expireTimes, SecretKey secretKey) {
        // 定义过期时间
        Date expireDate = new Date(System.currentTimeMillis() + expireTimes);
        return Jwts.builder()
                .subject(body).expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public String checkJWT(String pendingJWT) {

//        String userKey = jwtProperties.getKey();
        String userKey = JWT_KEY;
        log.info("Nacos jwt key = " + JWT_KEY);

        // 1. 对秘钥进行base64编码
        String base64 = Base64.getEncoder().encodeToString(userKey.getBytes());

        // 2. 对base64生成一个秘钥的对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());

        // 3. 校验jwt
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build(); // 构造解析器
        // 解析成功，可以获得Claims，从而去get相关的数据，如果此处抛出异常，则说明解析不通过，也就是token失效或者被篡改
        Jws<Claims> jws = jwtParser.parseSignedClaims(pendingJWT);      // 解析jwt

        return jws.getPayload().getSubject();
    }

}
