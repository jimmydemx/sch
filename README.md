
# 1, 项目文件的启动

SpringBootApplication自动装载的规则:
如果需要排除的话可以使用`exclude = DataSourceAutoConfiguration.class`

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

```