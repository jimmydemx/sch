主要实现功能是文件管理，文件上传。






对于文件上传大小限制的配置：配置了这些以后就如果超过限制，就会throw错误
注意为了在前端显示，还需要在interceptor中`GraceExceptionHandler`，捕获然后返回给前端

```yml
# 使用File, Path等操作文件
spring:
    servlet:
    multipart:
    max-file-size: 100MB   # 文件上传大小前置，设置最大值
    max-request-size: 100MB # 文件最大请求限制，用于批量上传

```
[MinioConfig.java](../hire-common/src/main/java/com/imooc/configs/MinioConfig.java)
上面存储的问题：文件都是存储在微服务的本地中，但是对于分布式系统如果微服务对应可能不同的服务器上面，这样就会分散存储。
MinIO可以作为云存储方案解决存储问题，五负端可以工作在windows以及linux上。

分布式存储的根本目的： 不同微服务在不同的服务器部署，一个服务器坏掉，不至于影响全局。

FastDFS: 安装配置复杂，出错
Ambry
MooseFS
MogileFS
LeoFS
不支持重命名

GlusterFS -> MinIO

