package io.dfjx.modules.auth.aop;

import io.dfjx.common.exception.RRException;
import io.dfjx.modules.auth.aop.annotation.OAuthAnn;
import io.dfjx.modules.auth.utils.UserThreadLocal;
import io.dfjx.modules.auth.vo.OnlineUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

@Aspect
@Component
public class OAuthAspect {
	private static final Logger logger = LoggerFactory.getLogger(OAuthAspect.class);

	@Pointcut("@annotation(io.dfjx.modules.auth.aop.annotation.OAuthAnn)")
	public void authPointCut() {

	}

	@Before("authPointCut()")
	public void saveSysLog(JoinPoint joinPoint) {
		// 获取request
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		OAuthAnn authAnn = method.getAnnotation(OAuthAnn.class);
		if (authAnn != null) {
			OnlineUser onlineUser = UserThreadLocal.get();
			if (!onlineUser.isIaAuth()) {
				return;
			}
			Set<String> permissions = onlineUser.getPermissions();
			String value = authAnn.value();
			String[] perms = value.split(",");
			for (String s:perms) {
				permissions.contains(s);
				return;
			}
			throw new RRException("无操作此权限，请联系管理授权！");
		}

	}

}
