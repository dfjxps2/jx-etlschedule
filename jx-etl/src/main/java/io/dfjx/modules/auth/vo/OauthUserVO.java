package io.dfjx.modules.auth.vo;

import java.io.Serializable;

/**
 * @author makaiyu
 * @date 2019/5/13 18:02
 */
public class OauthUserVO implements Serializable {

    private static final long serialVersionUID = 74335887421213094L;
    /** 主键id */
    private Long userId;

    private Long tenantId;

    /** 登录账号 */
    private String username;

    /** 名称 */
    private String name;


    private String userAvatar;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
