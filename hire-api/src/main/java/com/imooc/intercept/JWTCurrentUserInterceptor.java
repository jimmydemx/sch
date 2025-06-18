package com.imooc.intercept;

import com.imooc.Users;
import com.imooc.base.BaseInfoProperties;
import com.imooc.utils.GsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 使用ThreadLocal可以在同一线程中共享数据，包括mapper，service controller等层
 */
@Slf4j
@Component
public class JWTCurrentUserInterceptor extends BaseInfoProperties implements HandlerInterceptor {

    public static ThreadLocal<Users> currentUser = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info(">>> JWTCurrentUserInterceptor activated: {}", request.getRequestURI());
        String UserHeader = request.getHeader(APP_USER_JSON);
        String SaasHeader = request.getHeader(SAAS_USER_JSON);
        String AdminHeader = request.getHeader(ADMIN_USER_JSON);

        if (!StringUtils.isBlank(UserHeader) || !StringUtils.isBlank(SaasHeader)) {
            Users users = GsonUtils.stringToBean(UserHeader, Users.class);
            currentUser.set(users);
        }

        if (!StringUtils.isBlank(AdminHeader)) {

        }


        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        currentUser.remove();
    }
}
