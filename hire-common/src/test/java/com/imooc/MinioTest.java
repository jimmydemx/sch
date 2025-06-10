package com.imooc;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class MinioTest {

    @Test
    @SneakyThrows
    public void testUpload() {
        MinioClient minioClient = MinioClient.builder().endpoint("http://localhost:9000").credentials("root", "root123456").build();
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("localjava").build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("localjava").build());
        }


        minioClient.uploadObject(UploadObjectArgs.builder().bucket("localjava").object("bc.png").filename("D:\\face\\abc.png").build());

    }
}
