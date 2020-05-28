package io.dfjx.modules.auth.vo;

import java.io.Serializable;
import java.util.Set;

public class OnlineUser implements Serializable {

    private static final long serialVersionUID = -4327858532754502187L;

    private Long userId;

    private Long tenantId;

    private String username;

    private String realname;

    private String avatar;

    private String token;

    private boolean iaAuth = true;

    private Set<String> permissions;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIaAuth() {
        return iaAuth;
    }

    public void setIaAuth(boolean iaAuth) {
        this.iaAuth = iaAuth;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
