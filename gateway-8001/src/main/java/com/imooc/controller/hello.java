package com.imooc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequestMapping("/gateway")
@Tag(name="Hello",description = "Test Gateway")
public class hello {

    @GetMapping("/hello")
    @Operation(summary = "Hello",description = "Hello")
    public String hello() {
        return "Hello World";
    }
}
