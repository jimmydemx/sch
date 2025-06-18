package com.imooc.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/c")
@Tag(name = "hello world", description = "hello world for company")
public class hello {

    @Operation(summary = "hello world", description = "hello world for company")
    @GetMapping("/hello")
    private String helloWorld() {
        return "hello world";
    }
}
