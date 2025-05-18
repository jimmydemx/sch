package com.imooc.controller;


import com.imooc.grace.result.GraceJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("file")
@Slf4j
@Tag(name = "file hello",description = "file hello")
public class Hello {

    @GetMapping("/hello")
    @Operation(description = "file Hello",summary = "file hello")
    public String HelloFile() {
        return "hello file 5001";
    }
}
