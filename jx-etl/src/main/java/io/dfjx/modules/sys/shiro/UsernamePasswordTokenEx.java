package io.dfjx.modules.sys.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordTokenEx extends UsernamePasswordToken {

    private Long userId;

    public UsernamePasswordTokenEx(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
