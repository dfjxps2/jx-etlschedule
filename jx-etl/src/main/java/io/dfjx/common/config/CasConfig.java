package io.dfjx.common.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/11/27.
 */
@Configuration
@Component
public class CasConfig {

    @Value("${system-params.cas_server_url_prefix}")
    private String casServerUrl;
    @Value("${system-params.project_url}")
    private String projectUrl;

    /**
     * 用于实现单点登出功能
     */
//    @Bean
//    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
//        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
//        listener.setListener(new SingleSignOutHttpSessionListener());
//        listener.setOrder(Integer.MAX_VALUE - 10);
//        return listener;
//    }

    /**
     * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
     */
//    @Bean
//    public FilterRegistrationBean singleSignOutFilter() {
//        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
//        filterRegistration.setFilter(new SingleSignOutFilter());
//        filterRegistration.addUrlPatterns("/*");
//        filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrl + "/login?locale=zh_CN");
//        filterRegistration.addInitParameter("serverName", casServerUrl);
//        filterRegistration.setOrder(Integer.MAX_VALUE - 9);
//        return filterRegistration;
//    }

    /**
     * 该过滤器负责对Ticket的校验工作
     * @return
     */
    @Bean
    public FilterRegistrationBean ValidationFilterRegistrationBean(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", casServerUrl);
        initParameters.put("serverName", projectUrl);
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.setOrder(Integer.MAX_VALUE - 8);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }
    /**
     * 该过滤器负责用户的认证工作
     * @return
     */
    @Bean
    public FilterRegistrationBean authenticationFilterRegistrationBean() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AuthenticationFilter());
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", casServerUrl + "/login?locale=zh_CN");
        initParameters.put("serverName", projectUrl);
        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.setOrder(Integer.MAX_VALUE - 7);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/");
        urlPatterns.add("/index.html");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

    /**
     * 该过滤器对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
     * @return
     */
    @Bean
    public FilterRegistrationBean casHttpServletRequestWrapperFilter(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new HttpServletRequestWrapperFilter());
        authenticationFilter.setOrder(Integer.MAX_VALUE - 6);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

    /**
     * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
     比如AssertionHolder.getAssertion().getPrincipal().getName()。
     这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
     * @return
     */
    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AssertionThreadLocalFilter());
        authenticationFilter.setOrder(Integer.MAX_VALUE - 5);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }
}