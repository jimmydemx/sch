package com.imooc.controller;


import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.MinioUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;

@RequestMapping("files")
@Slf4j
@RestController
@Tag(name = "file control", description = "file opertations")
public class FileController {

    @Autowired
    private MinioUtils minioUtils;


    @PostMapping("/upload")
    @Operation(description = "upload File via minio", summary = "upload file")
    public GraceJSONResult uploadFile(@RequestParam("file") MultipartFile file) {
        String s = minioUtils.uploadFile(file);
        return GraceJSONResult.OK(s);
    }

    @GetMapping("/url")
    @Operation(description = "Get FileUrl via minio", summary = "Get fileUrl")
    public GraceJSONResult getFileUrl(@RequestParam String objectFileName) {
        String fileUrl = minioUtils.getFileUrl(objectFileName);
        return GraceJSONResult.OK(fileUrl);
    }


    @GetMapping("/download")
    @Operation(description = "download file via minio", summary = "download file")
    public GraceJSONResult downloadFile(@RequestParam String objectFileName, HttpServletResponse response) {
        try (InputStream is = minioUtils.downloadFile(objectFileName)) {
            // 2. 设置响应内容类型 告诉浏览器这是一个二进制文件下载 浏览器不会尝试解析内容 强制触发文件下载行为
            // 可以做如下替代： 例如对于图片
            //response.setContentType("image/jpeg")
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            // attachment：强制浏览器下载而非显示  filename：指定下载时保存的默认文件名
            // encodedFileName 是中文名字处理
            String encodedFileName = URLEncoder.encode(objectFileName, "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + encodedFileName + "\"");
            // 高效传输文件内容 零拷贝技术减少内存占用 支持大文件（GB级）下载 避免内存溢出（OOM）
            is.transferTo(response.getOutputStream());

            // 5. 刷新输出缓冲区 强制 Servlet 容器发送缓冲区内容 确保下载进度实时更新 避免部分数据滞留导致下载不完整
            response.flushBuffer();
            return GraceJSONResult.OK();

        } catch (Exception e) {
            log.error(e.getMessage());
            return GraceJSONResult.exception(ResponseStatusEnum.FILE_DOWNLOAD_FAILD);
        }

    }


    @DeleteMapping
    @Operation(description = "delete file via minio", summary = "delete file")
    public GraceJSONResult deleteFile(@RequestParam String objectFileName) {
        minioUtils.deleteFile(objectFileName);
        return GraceJSONResult.OK();
    }
}
