package com.imooc.utils;

import com.imooc.configs.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;


    @SneakyThrows
    public void ensureBucketExists() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
        }
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file) {
        ensureBucketExists();
        String objectFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectFileName).stream(
                file.getInputStream(),
                file.getSize(),
                -1
        ).contentType(file.getContentType()).build());
        return objectFileName;
    }


    @SneakyThrows
    public String getFileUrl(String objectFileName) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).
                bucket(minioConfig.getBucketName()).object(objectFileName).expiry(7, TimeUnit.DAYS).
                build());
    }

    @SneakyThrows
    public InputStream downloadFile(String objectFileName) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectFileName).build());

    }

    @SneakyThrows
    public void deleteFile(String objectFileName) {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectFileName).build());
    }
}
