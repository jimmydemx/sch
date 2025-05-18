> 常量既可以通过使用class中定义 `public static final`中实现,或者在配置项中，应该怎么样区分？

- class中定义的`public static final`完全不能改变
- 配置项中的是可以进行动态改变的

```java
package com.example.common.constants;
// 无依赖、全局统一
public class Constants {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REDIS_USER_PREFIX = "login:user:";
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String CHARSET_UTF8 = "UTF-8";
}
// -----------------在YAML中-------------------//
// verify:
// code:
// expire-seconds: 300

//根据这个设置配置类：


@Data
@Configuration
@ConfigurationProperties(prefix = "verify.code")
public class VerifyCodeProperties {
    private Integer expireSeconds;
}

@Service
public class VerifyCodeService {

    @Autowired
    private VerifyCodeProperties verifyCodeProperties;

    public void saveVerifyCode(String phone, String code) {
        verifyCodeProperties.setExpireSeconds(233);
        redisTemplate.opsForValue().set("verify:code:" + phone, code, verifyCodeProperties.getExpireSeconds(), TimeUnit.SECONDS);
    }
}

```