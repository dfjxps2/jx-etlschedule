///**
// * 2019 东方金信
// *
// *
// *
// *
// */
//
//package io.dfjx.modules.sys.oauth2;
//
//import com.google.gson.Gson;
//import io.dfjx.modules.sys.entity.SysUserEntity;
//import io.dfjx.modules.sys.entity.SysUserTokenEntity;
//import io.dfjx.modules.sys.service.ShiroService;
//import io.dfjx.modules.sys.shiro.ShiroUtils;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
///**
// * 认证
// *
// * @author Mark sunlightcs@gmail.com
// */
//@Component
//public class OAuth2Realm extends AuthorizingRealm {
//    @Autowired
//    private ShiroService shiroService;
//
//    private Logger logger = LoggerFactory.getLogger(OAuth2Realm.class);
//
//    @Override
//    public boolean supports(AuthenticationToken token) {
//        return token instanceof OAuth2Token;
//    }
//
//    /**
//     * 授权(验证权限时调用)
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//
//        logger.info("doGetAuthorizationInfo is running ");
//
//        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
//        Long userId = user.getUserId();
//
//        //用户权限列表
//        Set<String> permsSet = shiroService.getUserPermissions(userId);
//
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setStringPermissions(permsSet);
//        return info;
//    }
//
//    /**
//     * 认证(登录时调用)
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//
//        logger.info("doGetAuthenticationInfo is running ");
//
//        String accessToken = (String) token.getPrincipal();
//
//        //根据accessToken，查询用户信息
//        SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
//        //token失效
//        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
//            throw new IncorrectCredentialsException("token失效，请重新登录");
//        }
//
//        SysUserEntity userTmp =  ShiroUtils.getUserEntity();
//
//        if(userTmp==null){
//            userTmp = (SysUserEntity) ShiroUtils.getSessionAttribute("user");
//            ShiroUtils.setSessionAttribute("user",null);
//        }
//
//        logger.info("保存用户session信息",new Gson().toJson(userTmp));
//
////        //查询用户信息
////        SysUserEntity user = shiroService.queryUser(tokenEntity.getUserId());
////        //账号锁定
//        if(userTmp == null){
//            throw new IncorrectCredentialsException("用户缓存未找到，请重新登陆");
//        }
//
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userTmp, accessToken, getName());
//        return info;
//    }
//}
