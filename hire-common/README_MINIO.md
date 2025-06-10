docker 运行

```bash
docker run -d --restart=always \
  -p 9000:9000 \
  -p 9090:9090 \
  --name minio \
  -e "MINIO_ROOT_USER=root" \
  -e "MINIO_ROOT_PASSWORD=root123456" \
  -v C:/Users/Administrator/Desktop/Projects/sch/minio/data:/data \
  minio/minio server /data --console-address ":9090"

```


然后创建一个MinioConfig文件，用来读取``application.yml`中的相关配置`，注意:虽然`minioConfig`放置在了hire-common中，但是读取的配置需要是在当前运行的微服务中，比如运行service-file-5001微服务，需要读取的是service-file-5001`下面`的application.yml.

```java
package com.imooc.configs;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private boolean secure;

    @PostConstruct
    public void validate() {
        Assert.notNull(endpoint, "MinIO endpoint must not be null");
        Assert.notNull(accessKey, "MinIO accessKey must not be null");
        Assert.notNull(secretKey, "MinIO secretKey must not be null");
    }
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
}

```

最后把这个配置放入`MinioUtils.java`进行使用。

