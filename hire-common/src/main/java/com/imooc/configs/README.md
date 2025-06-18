
- 使用 ` @Component
  @Data
  @ConfigurationProperties(prefix = "auth")
  @PropertySource("classpath:jwt.properties")` 可以只用在`.properties`文件加载文件，`classpath`一般是`src/main/resources`下,
- 如果是`@PropertySource("classpath:config/custom.properties")` 表示`src/main/resources/config/` 下面的`custom.properties`文件


- [StaticResourceConfig.java](StaticResourceConfig.java) 是将static文件夹映射到本地一个文件夹中，这样可以直接通过`http://localhost:8001/static/abc.png`访问此图片。

```java
// 需要implement WebMvcConfigurer 然后在Override其中的addResourceHandlers的方法
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {}
}

```


[FeignSeataInterceptorConfiguration.java](FeignSeataInterceptorConfiguration.java) Seata通过`XID` 进行事务的管理,
对于不同的微服务(使用OpenFeign调用时候)，不能够自动加上XID，所以需要使用此Interceptor然后`手动`加上。
