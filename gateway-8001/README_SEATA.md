# 1. 关于docker中使用seata的步骤

## 1.1  本地化application.yml
因为包括端口，ip经常变化所以最好本地化此文件，本地化文件的步骤
配置IP是很重要的，使用`ipconfig`可以查看ip。也可以在nacos的服务端查看ip地址，
现在使用nacos服务端与其它idea的微服务相同的ip测试成功


```yaml
# 注意这个端口一定需要在配置在docker开放的port
server:
  port: 8091
# 相当于spring里面的配置
spring:
  application:
    name: seata-server
# 必须要配置secretKey， logging。file。path 以及 console.user.username 以及password 否则会报错
# 很重要就是serverAddr这里指向的电脑主机的address，这个一定需要配置正确，通过ipconfig进行查询

seata:
  security:
    secretKey: seataSecretKey123!
    tokenValidityInMilliseconds: 1800000    
  config:
    type: nacos
    nacos:
      serverAddr: 192.168.1.131:8848  
      group: SEATA_GROUP
      namespace: ""
  registry:
    type: nacos
    nacos:
      serverAddr: 192.168.1.131:8848
      namespace: ""
      cluster: default
      group: SEATA_GROUP
  # 数据库也必须要连接，seata有几张表必须要连接
  store:
    mode: db
    db:
      driverClassName: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/hire-seata?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
      user: root
      password: EJfKKkNc2H.
  service:
    vgroupMapping:
      my_tx_group: default
    default:
      grouplist: 192.168.1.131:8091

logging:
  file:
    path: /var/logs/seata
  level:
    io.seata: info
    org.springframework: warn
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n"
console:
  user:
    username: seata
    password: seata

```


## 1.2 运行docker

这里需要明确指定环境变量`-e SEATA_IP`, 否则有可能SEATA使用docker内部的IP地址

```bash
docker run -d \
  -p 8091:8091 \
  -e SEATA_IP=192.168.118.1 \
  -e SEATA_PORT=8091 \
  -v C:/Users/Administrator/Desktop/Projects/sch/application.yml:/seata-server/resources/application.yml \
  --name seata-server \
  seataio/seata-server:1.8.0

```

#
如果需要和nacos来连接必须要配置nacos，在nacos的配置中心设置一个新的配置
- Data ID service.vgroupMapping.my_tx_group (应该可以随便)
- Group: `必须`和上面SEATA中设置的nacos中的GROUP一致(SEATA_GROUP)
- 在具体配置中填入`default`就可以了。

在Spring boot中加入配置
```YML
seata:
  enabled: true
  service:
    vgroupMapping:
      my_tx_group: default
    grouplist:
      default: 192.168.1.131:8848
  tx-service-group: my_tx_group
  config:
    type: nacos
    # 和SEATA对于nacos中的配置对应
    nacos:
      server-addr: 192.168.1.131:8848
      group: SEATA_GROUP
      namespace: ""
  registry:
    type: nacos
    nacos:
      server-addr: 192.168.1.131:8848
      namespace: ""
      group: SEATA_GROUP

```


TM：
RM:连接DB管理具体的
TC：全局管理整体

TM对于一个新的任务需要向TC提交XID，一个全局的任务号，然后每一个RM进行Branch commit以及rollback

可以创建一个单独的数据库，然后在此[链接](https://github.com/seata/seata/blob/1.8.0/script/server/db/mysql.sql)创建表格

--
global_table
branch_table
lock_table
distribute lock
---
undo_log 每一个数据库需要一个建立一个表

```sql
CREATE TABLE `undo_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `branch_id` BIGINT(20) NOT NULL,
  `xid` VARCHAR(100) NOT NULL,
  `context` VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB NOT NULL,
  `log_status` INT(11) NOT NULL,
  `log_created` DATETIME NOT NULL,
  `log_modified` DATETIME NOT NULL,
  `ext` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

# 2 debug

## 2.1 连接不上的问题 `can not register RM,err: can not connect to services-server`

出现这个问题一般就是ip是有一定问题的，docker内部地址不是实际机器的地址，这个需要注意

## 2.2 连接上面，没有问题OpenFeign调用接口没有回退

需要通过检查调用接口是否有XID`System.out.println("当前 XID = " + RootContext.getXID());`
如果没有，说明SEATA没有自动的把XID加入到OpenFeign调用接口中，需要使用下面的进行全局加入

```java
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

```
注意RequestInterceptor是一个接口哦：

- 属于 Feign，不属于 Spring Boot 本身；所以需要需要引入 spring-cloud-starter-openfeign依赖
- 在 FeignClient 发出请求时生效；
- 并不是所有请求都走它，只拦截 Feign 请求。

对于Spring boot自身的拦截器使用 
```java
// 第一步 需要实现一个接口

@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("请求来了：" + request.getRequestURI());
        return true;
    }
}

// 第二步 手动注册到WebMvcConfigurer
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**");
    }
}


```

