package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.Users;
import com.imooc.bo.ModifyUserBO;
import com.imooc.mapper.UserModifyMapper;
import com.imooc.service.UserModifyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserModifyServiceImpl extends ServiceImpl<UserModifyMapper, Users> implements UserModifyService {

    @Autowired
    private UserModifyMapper userModifyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUser(ModifyUserBO userBO) {
        String userID = userBO.getUserID();
        Users user = new Users();
        user.setId(userID);
        user.setUpdatedTime(LocalDateTime.now());
        BeanUtils.copyProperties(userBO, user);
        userModifyMapper.updateById(user);
    }

    @Override
    public Users getUserByUserID(String userID) {
        return userModifyMapper.selectById(userID);
    }
}
