package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.Users;
import com.imooc.bo.InitRequest;
import com.imooc.enums.Sex;
import com.imooc.enums.ShowWhichName;
import com.imooc.enums.UserRole;
import com.imooc.feign.WorkMicroServiceFeign;
import com.imooc.mapper.UsersMapper;
import com.imooc.service.UsersService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.LocalDateUtils;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
//@Transactional(rollbackFor = Exception.class)
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private WorkMicroServiceFeign workMicroServiceFeign;

    @Autowired
    private UsersMapper usersMapper;

    private static final String USER_FACE1 = "";

    @Override
    @GlobalTransactional // Note: 如果使用多个微服务，不要使用Transactional，请使用seata的GlobalTransactional
    public Users createUser(String mobile) {
        Users user = new Users();

        // deal with name
        user.setMobile(mobile);
        user.setNickname("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setRealName("用户" + DesensitizationUtil.commonDisplay(mobile));
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

        usersMapper.insert(user);
        // 发起一个远程调用,初始化简历，新增一套空的记录
        InitRequest initRequest = new InitRequest();
        initRequest.setUserId(user.getId());
        workMicroServiceFeign.init(initRequest);

        log.info("全局事务 XID: {}", RootContext.getXID());

        try {
            int a = 1 / 0; // 业务代码
        } catch (Exception e) {
            log.error("事务异常，触发回滚", e);
            throw e; // 必须抛出，Seata 才会回滚
        }


        return user;
    }

    @Override
    public Users getUserByMobile(String mobile) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("mobile", mobile).orderByDesc("id").last("limit 1");
        return usersMapper.selectOne(usersQueryWrapper);
    }
}
