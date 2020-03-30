package io.dfjx.modules.auth.aop.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author looyoo
 * @email service@gmail.com
 * @date 2017年3月8日 上午10:19:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OAuthAnn {

	String value() default "";
}
