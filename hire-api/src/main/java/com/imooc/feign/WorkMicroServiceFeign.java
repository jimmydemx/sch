package com.imooc.feign;

import com.imooc.bo.InitRequest;
import com.imooc.grace.result.GraceJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("work-service")
public interface WorkMicroServiceFeign {

    @PostMapping("/resume/init")
    public GraceJSONResult init(@RequestBody InitRequest request);


    @GetMapping("/work/hello")
    public String hello();
}
