
[TOC]


# 1, 项目文件的启动


README 结构：

> - hire-common: 
   >> - JWT: 有所有关于JWT的相关配置
   >> - MINIO: 关于文件MINIO相关配置
> - hire-pojo
   >> - My-Batis Plug: 数据库的相关配置
> - gateway-8001
   >> - SEATA: 不同微服务的事务回滚 





SpringBootApplication自动装载的规则:
如果需要排除的话可以使用`exclude = DataSourceAutoConfiguration.class`

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

```


## kill port

```bash
netstat -ano | findstr :8080
taskkill /PID 12345 /F
```




## 关于debug问题

```java

```
