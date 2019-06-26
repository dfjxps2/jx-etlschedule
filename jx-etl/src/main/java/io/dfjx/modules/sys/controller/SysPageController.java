/**
 * Copyright 2018 东方金信
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.dfjx.modules.sys.controller;

import io.dfjx.common.config.SystemParams;
import io.dfjx.common.synchrodata.Dom4jUtil;
import io.dfjx.common.synchrodata.PortalFilter;
import io.dfjx.common.synchrodata.SynchronizedDataConstants;
import io.dfjx.common.synchrodata.WebClient;
import io.dfjx.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统页面视图
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月24日 下午11:05:27
 */
@Controller
public class SysPageController {

	@Autowired
	private SystemParams systemParams;
	
	@RequestMapping("modules/{module}/{url}.html")
	public String module(@PathVariable("module") String module, @PathVariable("url") String url){
		return "modules/" + module + "/" + url;
	}

	@RequestMapping(value = {"/", "index.html"})
	public String index(HttpServletRequest request){
		return ssoLogin(request);
		//return "index";
	}

	@RequestMapping("index1.html")
	public String index1(){
		return "index1";
	}

	@RequestMapping("login.html")
	public String login(){
		return "login";
	}

	@RequestMapping("main.html")
	public String main(){
		return "main";
	}

	@RequestMapping("404.html")
	public String notFound(){
		return "404";
	}

	@RequestMapping("logincas")
	public String logincas(){
		return "redirect:"+systemParams.getPortalUrl();
	}

	public String ssoLogin(HttpServletRequest request){
		String main = "index";
		PortalFilter sso = new PortalFilter();
		boolean isLogin = sso.doLogin(request);
		if(!isLogin){
			return "redirect:"+systemParams.getPortalUrl();
		}
		return main;
	}
}
