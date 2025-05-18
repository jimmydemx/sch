package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.Users;


public interface UsersService extends IService<Users> {

    public Users createUser(String mobile);

    public Users getUserByMobile(String mobile);
}
