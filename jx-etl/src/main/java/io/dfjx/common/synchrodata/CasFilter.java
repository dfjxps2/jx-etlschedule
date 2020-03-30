//package io.dfjx.common.synchrodata;
//
//import io.dfjx.common.config.SystemParams;
//import io.dfjx.modules.sys.service.SysUserService;
//import io.dfjx.modules.sys.shiro.ShiroUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.subject.Subject;
//import org.jasig.cas.client.authentication.AttributePrincipal;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//import java.security.Principal;
//
///**
// * Created by cc on 2018/11/22.
// */
//public class CasFilter /*extends AccessControlFilter*/ {
//    private static final Logger log= LoggerFactory.getLogger(CasFilter.class);
//
//    private SysUserService sysUserService;
//    private SystemParams systemParams;
//
//    public boolean doLogin(HttpServletRequest request){
//        boolean isLogin = ShiroUtils.isLogin();
//        if(isLogin){
//            return true;
//        }
//        //如果未登录，读取cas信息
//        String casUserId = getCasName(request);
//        if(StringUtils.isBlank(casUserId)){
//            warn("casUserCookieId为空，无法获取门户用户信息");
//            if(StringUtils.isBlank(casUserId))
//                return false;
//        }
//
//        ServletContext sc = request.getServletContext();
//        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
//        sysUserService = ctx.getBean(SysUserService.class);
//        systemParams = ctx.getBean(SystemParams.class);
//
//        //设置已登录
//        Subject subject = ShiroUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(casUserId, "无需登录密码");
//        subject.login(token);
//        return true;
//    }
//
//    private String getCasName(HttpServletRequest request){
//        String username = request.getRemoteUser();
//        if(StringUtils.isNotBlank(username))
//            return username;
//        AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
//        if(principal != null){
//            username = principal.getName();
//            if(username != null)
//                return username;
//        }
//        Principal pal = request.getUserPrincipal();
//        if(pal != null){
//            username = pal.getName();
//            if(username != null)
//                return username;
//        }
//        Object obj = request.getAttribute("credentials");
//        if(obj != null)
//            return obj.toString();
//        return username;
//    }
//    private void warn(String msg){
//        log.warn(msg);
//        //System.out.println(msg);
//    }
//}
