package io.dfjx.modules.auth.filter;


import io.dfjx.common.utils.SpringContextUtils;
import io.dfjx.modules.auth.service.AuthService;
import io.dfjx.modules.auth.utils.CookieUtils;
import io.dfjx.modules.auth.utils.UserThreadLocal;
import io.dfjx.modules.auth.vo.OnlineUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class LoginInterceptor implements HandlerInterceptor {

    private static AuthService authService = (AuthService) SpringContextUtils.getBean("authService");

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String redirectUrl = httpServletRequest.getContextPath() + "/logincas";

        String token = CookieUtils.GetToken(httpServletRequest);
        if(!authService.checkLogin(httpServletRequest)){
            httpServletResponse.sendRedirect(redirectUrl);
            return false;
        }
        OnlineUser user = authService.nowOnlineUser(token, httpServletRequest, httpServletResponse);
        if(null == user){
            httpServletResponse.sendRedirect(redirectUrl);
            return false;
        }
        user = authService.queryUsersByIds(user.getUserId());
        user.setToken(token);

        Set<String> permissions = authService.selectPermissionsByUserIdAndSystemToSet(user.getUserId());
        user.setPermissions(permissions);

        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserThreadLocal.remove();
    }
}
