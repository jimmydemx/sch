练习: 设计一个注解叫做@OperationLog
 # 注解
```java
{
  "title": "产品管理", //传入
  "businessType": "新增产品", //传入 
  "method": "com.example.service.ProductService.addProduct",//方法
  "username": "admin", // 到底是谁调用
  "params": "[\"iPhone\",6999.0]", //
  "result": "\"添加产品成功：iPhone\"", // 结果
  "duration": 15, //消耗时间
  "success": true // 调用是否成功
}

```

# 关于AOP中使用Around

> @Around("execution(* com.imooc.service.. *.*(..))")
>> - \*： 任意返回值 \
>> - com.imooc.service..：com.imooc.service 包及其子包
>> - \*.*(..)	匹配所有类的所有方法，且方法参数为任意类型和数量（包括无参数）


注意使用`@annotation(operationLog)` 才能够在calculateOperation函数中使用`OperationLog operationLog`作为参数，
注意@annotation+ instance `不是OperationLog`;

```java
 @Around("@annotation(operationLog) && execution(* com.imooc.service.impl..*.*(..))")
    public Object calculateOperation(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {}
```