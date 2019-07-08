package io.dfjx.modules.sys.controller;

import com.bjca.sso.bean.UserTicket;
import com.bjca.sso.processor.TicketManager;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import io.dfjx.common.config.GovRedirectConfig;
import io.dfjx.common.utils.R;
import io.dfjx.modules.etl.entity.GovCaUserForm;
import io.dfjx.modules.sys.entity.SysUserEntity;
import io.dfjx.modules.sys.service.SysUserTokenService;
import io.dfjx.modules.sys.shiro.ShiroUtils;
import io.dfjx.modules.sys.shiro.UsernamePasswordTokenEx;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@Api("太极登陆CONTROLLER")
public class TaijiCaLoginController extends AbstractController{

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private GovRedirectConfig govRedirectConfig;

    /**
     * CA认证中心跳转过来的接口
     * @throws IOException
     */
    @PostMapping("/")
    @ApiOperation("太极登陆接口")
    public void cleanseRedirect(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("gbk");

        GovCaUserForm form = new GovCaUserForm();
        form.setBjcaTicket(request.getParameter("BJCA_TICKET"));
        form.setBjcaTicketType(request.getParameter("BJCA_TICKET_TYPE"));
        form.setBjcaTargetUrl(request.getParameter("BJCA_TARGET_URL"));
        form.setBjcaServerCert(request.getParameter("BJCA_SERVER_CERT"));

        R loginResult = this.login(form, null);
//

        if(loginResult.containsKey("code")&&!"0".equals(String.valueOf(loginResult.get("code")))){

            logger.info("login failed :{}",loginResult);

        }else{
            logger.info("login success :{}",loginResult);

            Object token = loginResult.get("token");
            String redirectUrl = new StringBuilder(govRedirectConfig.getCleanseWebUrl())
                    .append("?token=").append(token).append("&type=").append("cleanse").toString();

            logger.info("Redirect url ====> {}",redirectUrl);
            this.doRedirect(redirectUrl,response);

        }
    }

    /**
     * CA认证中心跳转过来的接口
     * @param form
     * @param response
     * @throws IOException
     */
    @PostMapping("desensitize")
    @ApiOperation("太极登陆接口")
    public void desensitizeRedirect(@RequestParam GovCaUserForm form, HttpServletResponse response) throws IOException {

        R loginResult = this.login(form, null);


        if(loginResult.containsKey("code")){

        }else{
            Object token = loginResult.get("token");
            String redirectUrl = new StringBuilder(govRedirectConfig.getDesensitizeWebUrl())
                    .append("?token=").append(token).append("&type=").append("desensitize").toString();
            this.doRedirect(redirectUrl,response);
        }

    }

    private void doRedirect(String redirectUrl,HttpServletResponse response) throws IOException {
        response.sendRedirect(redirectUrl);
    }

    @PostMapping(value = {"/ca/login/{mockUserId}","/ca/login"})
    @ApiOperation("太极ajax登陆接口")
    public R login(@RequestBody GovCaUserForm form,
                                     @PathVariable(value = "mockUserId", required = false) String mockUserId)  {
        if(!Strings.isNullOrEmpty(mockUserId)){
            return this.mockLogin(Long.parseLong(mockUserId));
        }

        logger.info("Send to Ca =========== /n {}",new Gson().toJson(form));

        TicketManager ticketmag = new TicketManager();

        String BJCA_TICKET = form.getBjcaTicket();
        String BJCA_TICKET_TYPE = form.getBjcaTicketType();
        String BJCA_SERVER_CERT = form.getBjcaServerCert();

        //验证签名及解密
        UserTicket userticket = ticketmag.getTicket(BJCA_TICKET, BJCA_TICKET_TYPE, BJCA_SERVER_CERT);
        //处理票据信息
        if(userticket != null) {
            logger.info("CA response :{}", new Gson().toJson(userticket));

            //用户姓名
            String username = userticket.getUserName();//这个是由“BJCA公司” 配置的选项，如果没有值需要告知“BJCA公司”。
            //用户3	2位唯一标识码
            String userid = userticket.getUserUniqueID();
            //用户所在部门的编码
            String departcd = userticket.getUserDepartCode();
            //用户所在部门的名称
            String departname = userticket.getUserDepartName();
//
//            TnmtEntity tnmtEntity = tnmtService.getTnmtByDepart(departcd);

            SysUserEntity sysUserEntity = new SysUserEntity();
            sysUserEntity.setUserId(Long.parseLong(userid));
            sysUserEntity.setUsername(username);
            //sysUserEntity.setTenantId(new Long(tnmtEntity.getTnmtid()));

            ShiroUtils.setSessionAttribute("user",sysUserEntity);

            //用户所拥有的角色信息
//            Hashtable roles = userticket.getUserRoles();
//            String s_role = "";
//            if(roles != null && roles.size() > 0) {
//                int index = 1;
//                Enumeration e = roles.keys();
//                Enumeration e2 = roles.elements();
//                for(;e.hasMoreElements();){
//                    String rolecode = (String)e.nextElement();
//                    String rolename = (String)e2.nextElement();
//                    if(rolename.indexOf("?") != -1) {
//                        rolename = new String(rolename.getBytes("ISO-8859-1"),"GB2312");
//
//                    }
//                    if(index == 1){
//                        s_role = rolecode;
//                    }else{
//                        s_role = s_role + "," + rolecode;
//                    }
//                    index++;
//                    System.out.println("s_role======="+s_role);
//                    System.out.println("rolename======="+rolename);
//                }
//            }

            R createResult = sysUserTokenService.createToken(sysUserEntity.getUserId().toString());
            return createResult;

        }else{
            logger.error("CA认证中心用户查找失败:{}",new Gson().toJson(form));
//            response.sendRedirect("sso_errors.jsp");//这是测试的错误页面。
            return R.error("CA认证中心用户查找失败");
        }
    }

    @Value("${ca.valid}")
    private boolean caVaid;

    @GetMapping("/ca/logout")
    @ApiOperation("太极登出接口")
    public Map<String, Object> logout() {

        if (caVaid) {
            sysUserTokenService.logout(ShiroUtils.getUserEntity().getUserId().toString());
            SecurityUtils.getSubject().logout();
        }

        return R.ok();
    }

    private R mockLogin(Long userId){
        R createResult = sysUserTokenService.createToken(userId.toString());
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(userId);
        sysUserEntity.setUsername("mockUser");

        ShiroUtils.setSessionAttribute("user", sysUserEntity);

        Subject subject = ShiroUtils.getSubject();
        UsernamePasswordTokenEx token = new UsernamePasswordTokenEx(userId);
        subject.login(token);
        return createResult;
    }

}
