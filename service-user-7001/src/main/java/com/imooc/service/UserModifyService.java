package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.Users;
import com.imooc.bo.ModifyUserBO;
import com.imooc.vo.Resume;

public interface UserModifyService extends IService<Users> {

    public void modifyUser(ModifyUserBO userBO);

    public Users getUserByUserID(String userID);
}
