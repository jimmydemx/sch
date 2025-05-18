package com.imooc.controller;


import com.imooc.grace.result.GraceJSONResult;
import com.imooc.mapper.UsersMapper;
import com.imooc.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/a")
@Tag(name="user",description = "create user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/create_user")
    @Operation(summary = "Create User",description = "Create User")
    public GraceJSONResult createUser(String mobile) {
        usersService.createUser(mobile);
        return GraceJSONResult.OK();
    }
}
