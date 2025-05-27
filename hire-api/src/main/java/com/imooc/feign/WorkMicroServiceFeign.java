package com.imooc.feign;

import com.imooc.bo.InitRequest;
import com.imooc.grace.result.GraceJSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "work-service") //  configuration = FeignSeataInterceptorConfiguration.class
public interface WorkMicroServiceFeign {

    @PostMapping("/resume/init")
    public GraceJSONResult init(@RequestBody InitRequest request);


    @GetMapping("/work/hello")
    public String hello();
}
