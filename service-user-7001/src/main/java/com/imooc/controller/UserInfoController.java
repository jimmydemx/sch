package com.imooc.controller;

import com.google.gson.Gson;
import com.imooc.Users;
import com.imooc.bo.ModifyUserBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.mapper.UserModifyMapper;
import com.imooc.service.UserModifyService;
import com.imooc.utils.JWTUtils;
import com.imooc.vo.UsersVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.imooc.base.BaseInfoProperties.TOKEN_USER_PREFIX;

@RestController
@RequestMapping("userinfo")
@Slf4j
@Tag(name = "Modify user",description = "Modify user info.")
public class UserInfoController {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserModifyService userModifyService;

    @PostMapping("/modify")
    @Operation(summary = "modify user",description = "modify user info.")
    public GraceJSONResult ModifyUser(@RequestBody ModifyUserBO modifyUserBO) {
        log.info("Modify user info start");
    userModifyService.modifyUser(modifyUserBO);

    UsersVO usersVO =getUsersVO(modifyUserBO.getUserID());
    return GraceJSONResult.OK(usersVO);
    }

    private UsersVO getUsersVO(String userID) {
        Users userByUserID = userModifyService.getUserByUserID(userID);
        String jwtWithPrefix = jwtUtils.createJWTWithPrefix(new Gson().toJson(userByUserID), TOKEN_USER_PREFIX);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userByUserID,usersVO);
        usersVO.setUserToken(jwtWithPrefix);
        return usersVO;
    }
}
