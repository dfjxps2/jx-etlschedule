package io.dfjx.common.synchrodata;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.dfjx.common.config.SystemParams;
import io.dfjx.modules.sys.entity.SysUserEntity;
import io.dfjx.modules.sys.service.SysUserService;
import io.dfjx.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/11/22.
 */
public class PortalFilter /*extends AccessControlFilter*/ {
    private static final Logger log= LoggerFactory.getLogger(PortalFilter.class);

    private SysUserService sysUserService;
    private SystemParams systemParams;

//    @Override
//    public boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object object) throws Exception{
//        String url = getPathWithinApplication(servletRequest);
//        System.out.println(url);
//        return false;
//    }

//    @Override
    public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception{
        //doLogin((HttpServletRequest)request);
        return true;
    }

    public boolean doLogin(HttpServletRequest request){
        boolean isLogin = ShiroUtils.isLogin();
        if(isLogin){
            return true;
        }
        //如果未登录，读取cas信息
        String casUserId = getCasName(request);
        if(StringUtils.isBlank(casUserId)){
            warn("casUserCookieId为空，无法获取门户用户信息");
            if(StringUtils.isBlank(casUserId))
                return false;
        }

        ServletContext sc = request.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
        sysUserService = ctx.getBean(SysUserService.class);
        systemParams = ctx.getBean(SystemParams.class);

        Map<String, Object> mod1 = getPortalInfo(casUserId, systemParams.getPortalApp(), systemParams.getSoapUrl());
        if(mod1 == null){
            warn("远程服务查询结果为空，无法获取门户用户信息");
            return false;
        }

        //获取本地信息并比较
        Map<String, Object> mod2 = sysUserService.selectMap(new EntityWrapper<SysUserEntity>().eq("username", casUserId));
        //数据同步，用户不存在时新增，用户名字不一样时更新
        if(mod2 == null && mod1 != null){
            SysUserEntity entity = new SysUserEntity();
            entity.setUsername(mod1.get("user_name").toString());
            entity.setDeptId(1L);   //默认部门1
            entity.setStatus(1);
            entity.setPassword("etl123"); //默认密码
            List<Long> roles = new ArrayList<>();
            roles.add(1L);   //默认访问者，最小权限
            entity.setRoleIdList(roles);
            sysUserService.save(entity);
            //save to mod2
            mod2 = new HashMap<>();
            mod2.put("password", entity.getPassword());
        }
        //设置已登录
        Subject subject = ShiroUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(casUserId, "无需登录密码");
        subject.login(token);
        return true;
    }

    private Map<String,Object> getPortalInfo(String casUserId, String appName, String soapUrl){
        WebClient web = new WebClient();
        Map<String,Object> mp = new HashMap<>();

        mp.put("arg0",appName);
        mp.put("arg1",casUserId);
        String operationName = SynchronizedDataConstants.GET_ONEUSER_WSDL_OPERATION_NAME;

        //	 mp.put(GET_ONEUSER_WSDL_OPERATION_NAME_KEY,GET_ONEUSER_WSDL_OPERATION_NAME);
        String retXml = web.getWsdlResultByCode(mp,operationName, soapUrl); //传入参数名，参数值，方法名
        List<Map<String,Object>> retList = Dom4jUtil.readDom4jXml(retXml);
        if(retList != null && retList.size()>0)
            return retList.get(0);
        return null;
    }
    private String getCasName(HttpServletRequest request){
        String username = request.getRemoteUser();
        if(StringUtils.isNotBlank(username))
            return username;
        Principal pal = request.getUserPrincipal();
        if(pal != null){
            username = pal.getName();
            if(username != null)
                return username;
        }
        Object obj = request.getAttribute("credentials");
        if(obj != null)
            return obj.toString();
        return username;
    }
    private void warn(String msg){
        log.warn(msg);
        //System.out.println(msg);
    }
}
