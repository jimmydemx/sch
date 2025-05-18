package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bo.InitRequest;
import com.imooc.enums.Sex;
import com.imooc.enums.ShowWhichName;
import com.imooc.enums.UserRole;
import com.imooc.feign.WorkMicroServiceFeign;
import com.imooc.mapper.UsersMapper;
import com.imooc.service.UsersService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.LocalDateUtils;
import com.imooc.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private WorkMicroServiceFeign workMicroServiceFeign;

    @Autowired
    private UsersMapper usersMapper;

    private static final String USER_FACE1 = "";

    @Override
    public Users createUser(String mobile) {
        Users user = new Users();

        // deal with name
        user.setMobile(mobile);
        user.setNickname("用户"+ DesensitizationUtil.commonDisplay(mobile));
        user.setRealName("用户"+ DesensitizationUtil.commonDisplay(mobile));
        user.setShowWhichName(ShowWhichName.nickname.type);

        //
        user.setSex(Sex.secret.type);
        user.setFace(USER_FACE1);
        user.setEmail("");

        LocalDate birthday = LocalDateUtils.parseLocalDate("1980-01-01", LocalDateUtils.DATE_PATTERN);
        user.setBirthday(birthday);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");

        user.setDistrict("");
        user.setDescription("这个家伙很懒，什么都没留下~");

        user.setStartWorkDate(LocalDate.now());
        user.setPosition("底层代码");
        user.setRole(UserRole.CANDIDATE.type);
        user.setHrInWhichCompanyId("");

        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

//        usersMapper.insert(user);
        // 发起一个远程调用,初始化简历，新增一套空的记录
        InitRequest initRequest = new InitRequest();
        initRequest.setUserId(user.getId());
        workMicroServiceFeign.init(initRequest);

        return user;



    }

    @Override
    public Users getUserByMobile(String mobile) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("mobile",mobile).orderByDesc("id").last("limit 1");
        return usersMapper.selectOne(usersQueryWrapper);
    }
}
