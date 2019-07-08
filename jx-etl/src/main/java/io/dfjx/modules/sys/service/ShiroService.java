/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjx.modules.sys.service;


import io.dfjx.modules.sys.entity.SysUserEntity;
import io.dfjx.modules.sys.entity.SysUserTokenEntity;

import java.util.Set;

/**
 * shiro相关接口
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(Long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}
