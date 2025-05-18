使用ServiceFeign需要注意：

- ✅ 已添加 spring-cloud-starter-openfeign 依赖
- ✅ 已添加 spring-cloud-starter-loadbalancer 依赖
- ✅ 创建CLass使用了 @FeignClient(name = "work-service") 创建mapper
- ✅ 在需要使用的服务入口程序有 @EnableFeignClients 注解，并扫描到接口所在包，比如@EnableFeignClients(basePackages = "com.imooc.feign")
- ✅ 配置了 spring.application.name 且已成功注册到 Nacos

问题: 
这么多必要始终没有能够整合和关注到一起的？

GraceJSONResult 必须要有一个构造函数才能够JSON → Java对象）时，需要能“new 一个对象”，默认只能通过无参构造函数来创建实例。
在feign中，显示返回一个被`Jackson` 转化的JSON，然后这个JSON在`workMicroServiceFeign.init(initRequest)`,需要先变为Java对象，
然后再变为JSON返回前端，但是JSON → Java对象过程中GraceJSONResult需要有一个无参的构造函数，否者会有问题

```java
 @PostMapping("/resume/init")
    public GraceJSONResult init(@RequestBody InitRequest request);

@PostMapping("/test-feign")
@Operation(summary = "test Feign Post",description = "test Feign POST")
public GraceJSONResult getHelloWorldFromWork(){
    InitRequest initRequest = new InitRequest();
    initRequest.setUserId("12345");
    return workMicroServiceFeign.init(initRequest);
}

//
ObjectMapper mapper = new ObjectMapper();
GraceJSONResult result = mapper.readValue(json, GraceJSONResult.class);

```

Jackson 的流程是：

- 尝试使用无参构造函数 new GraceJSONResult() 创建对象实例；
- 然后根据 JSON 中的字段，依次调用 setter（比如 setStatus(...)）将数据填充进去；
- 如果类没有无参构造函数，而 Jackson 又没有其他能用的「构造方式」，就会报错。
