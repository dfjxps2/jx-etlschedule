package io.dfjx.common.utils;

import com.dfjinxin.commons.auth.utlis.OnlineUserUtils;
import com.seaboxdata.auth.api.utils.UserUtils;
import com.seaboxdata.auth.api.vo.OauthLoginUserVO;
import io.dfjx.modules.sys.entity.SysUserEntity;
import io.dfjx.modules.sys.service.SysUserService;


public class TagUserUtils {

    private static SysUserService sysUserService = (SysUserService) SpringContextUtils.getBean("sysUserService");

    public static Long userId(){
//        return SecurityUtils.getUser().getUserId();

        return OnlineUserUtils.userId();
//        return 1L;
    }

    public static String userName(){
        return OnlineUserUtils.username(userId(), null);
//        return "admin";
    }

    public static SysUserEntity getTagUser(){
        OnlineUserUtils.getOauthLoginUserVO();
        SysUserEntity userEntity = sysUserService.queryUserById(userId());
        return userEntity;
    }

}
