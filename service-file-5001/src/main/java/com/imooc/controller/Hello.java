package com.imooc.controller;


import com.imooc.grace.result.GraceJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("file")
@Slf4j
@Tag(name = "file hello", description = "file hello")
public class Hello {

    @GetMapping("/hello")
    @Operation(description = "file Hello", summary = "file hello")
    public String HelloFile() {
        return "hello file 5001";
    }


    @Operation(description = "upload file for avatar of user", summary = "upload Avatar")
    @PostMapping("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam("file") MultipartFile file,
                                      @RequestParam("userId") String userId,
                                      HttpServletRequest httpServletRequest) throws IOException {


        // 1. 空文件校验
        if (file.isEmpty()) {
            return GraceJSONResult.errorMsg("上传文件为空");
        }

        // 2. 获取文件名和后缀
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

        // 3. 白名单校验
        if (!extension.matches("\\.(jpg|jpeg|png|gif)$")) {
            return GraceJSONResult.errorMsg("文件格式不支持");
        }

        // 4. 构建保存路径
        String fileName = userId + extension;
        Path folder = Paths.get("D:", "face");
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        // 5. 保存文件
        Path fullPath = folder.resolve(fileName);
        file.transferTo(fullPath.toFile());

        return GraceJSONResult.OK();

    }
}
