package com.imooc.controller;

import com.imooc.bo.RegisterLoginBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.retry.RetryComponent;
import com.imooc.utils.IpUtils;
import com.imooc.utils.MobileValidation;
import com.imooc.utils.RedisOperators;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/passport")
@Tag(name = "passport",description = "passport control")
public class PassportController {

    @Autowired
    private RedisOperators redisOperators;

    @GetMapping("/hello")
    @Operation(summary = "hello",description = "test api")
    public GraceJSONResult Hello(){
        return GraceJSONResult.OK("hello world");
    }

    @GetMapping("/helloRedis")
    @Operation(summary = "helloRedis",description = "test Redis")
    public GraceJSONResult HelloRedis(){
        redisOperators.setString("hello","word",1000);
        return GraceJSONResult.OK("hello redis");
    }


    @PostMapping("getsmscode")
    @Operation(summary ="SMS CODE",description = "get SMS code")
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request){
        // 判断是否是正确的module号码
       if(!MobileValidation.isPhoneValidated(mobile)){
           return GraceJSONResult.errorCustom(ResponseStatusEnum.MOBILE_FORMAT_ERROR);
       }
        // 获得用户IP，限制用户只能60s
        String requestIp = IpUtils.getRequestIp(request);
        redisOperators.setnx60s("MOBILE_SMSCODE_"+requestIp,mobile);
        // 模拟生成随机的验证码
        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        log.info("验证码为: {}",code);
        // 生成以后，存入redis，等待过期
        redisOperators.setString("MOBILE_SMSCODE_"+mobile,code,1800);
        return GraceJSONResult.OK();
    }

    @PostMapping("/login")
    @Operation(summary = "LOG IN",description = "LOG IN")
    public GraceJSONResult login(@Valid @RequestBody RegisterLoginBO registerLoginBO, HttpServletRequest request){

        // 在redis中寻找，如果找不到，返回错误
        String key = "MOBILE_SMSCODE_"+registerLoginBO.getMobile();
        String code = registerLoginBO.getSmsCode();
        String redisCode = redisOperators.getValue(key);

        if(StringUtils.isBlank(redisCode)|| !redisCode.equalsIgnoreCase(code)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 在数据库中查找，是否有这个手机号，


        // 如果数据为空，必须要信息入库

        // 范围jwttoken

        // 删除redis短信验证码

        // 返回userVO


        return GraceJSONResult.OK();
    }



}
