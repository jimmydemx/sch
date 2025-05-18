package com.imooc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/work")
@Tag(name="work",description = "work apis")
public class hello {

    @Value("${server.port}")
    private String port;

    @Operation(summary = "test api",description = "hello world")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World，work,";
    }

    @Operation(summary = "test port",description = "test hello port")
    @GetMapping("/hello-port")
    public String helloPort() {
        return "Hello World，port is:"+port;
    }



}
