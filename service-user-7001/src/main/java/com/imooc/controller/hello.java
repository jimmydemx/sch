package com.imooc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "示例API", description = "这是一个示例接口")
public class hello {
    @GetMapping("/hello")
    @Operation(summary = "问候接口", description = "返回一个欢迎信息")
    private String helloWord(){
        return "hello world, service-user";
    }
}
