package com.imooc.controller;


import com.imooc.bo.InitRequest;
import com.imooc.feign.WorkMicroServiceFeign;
import com.imooc.grace.result.GraceJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passport")
@Tag(name="Test Feign",description = "works on how to create feign")
public class TestFeignController {

    @Autowired
    private WorkMicroServiceFeign workMicroServiceFeign;


@PostMapping("/test-feign")
@Operation(summary = "test Feign Post",description = "test Feign POST")
public GraceJSONResult getHelloWorldFromWork(){
    InitRequest initRequest = new InitRequest();
    initRequest.setUserId("12345");
    return workMicroServiceFeign.init(initRequest);
}

@GetMapping("/hello-feign")
@Operation(summary ="hello feign",description = "test Feign GET")
public GraceJSONResult getHelloWorld(){
    String hello = workMicroServiceFeign.hello();
    return GraceJSONResult.OK(hello);
}
}
