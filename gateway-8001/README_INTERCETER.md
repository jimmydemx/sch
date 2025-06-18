


> Spring Boot 只会自动注册过滤器（Filter）和切面（Aspect），不会自动注册 HandlerInterceptor。
> Spring Boot 的自动配置类 WebMvcAutoConfiguration 是默认启用的，但其中的拦截器注册，只通过 WebMvcConfigurer 暴露，不会自动扫描你的 @Component 的 HandlerInterceptor 并注册到 InterceptorRegistry。
> HandlerInterceptor 需要你明确告诉 Spring：它拦截哪些路径、以什么顺序执行；




## Spring有那些AOP的设计？ Filter, Interceptor ,OncePerRequestFilter, AOP

```java
//


@Component
public class SecurityFilterJWT implements GlobalFilter, Ordered {
}


// 


// 注册对于那些url郑家interceptor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/auth/**"); // no token required
    }
}


```


## 比较OncePerRequestFilter 和 GlobalFilter

| 名称                     | 所属框架                     | 主要用途                          |
| ---------------------- | ------------------------ | ----------------------------- |
| `OncePerRequestFilter` | Spring MVC / Spring Boot | 拦截 Servlet 请求（基于 Servlet API） |
| `GlobalFilter`         | Spring Cloud Gateway     | 拦截 WebFlux 异步流式请求（基于 Reactor） |





## threadLocal 