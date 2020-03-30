package io.dfjx.common.utils;

import io.dfjx.modules.auth.utils.UserThreadLocal;
import io.dfjx.modules.auth.vo.OnlineUser;
import io.dfjx.modules.sys.entity.SysUserEntity;


public class TagUserUtils {

    public static Long userId(){
        OnlineUser onlineUser = UserThreadLocal.get();
        return onlineUser.getUserId();
    }

    public static String userName(){
        OnlineUser onlineUser = UserThreadLocal.get();
        return onlineUser.getUsername();
    }

    public static SysUserEntity getTagUser(){
        OnlineUser onlineUser = UserThreadLocal.get();
        SysUserEntity entity = new SysUserEntity();
        entity.setUsername(onlineUser.getUsername());
        entity.setUserId(onlineUser.getUserId());
        return entity;
    }

}
