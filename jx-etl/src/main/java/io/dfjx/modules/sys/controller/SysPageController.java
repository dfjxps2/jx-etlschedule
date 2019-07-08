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

import com.google.gson.Gson;
import io.dfjx.common.config.SystemParams;
import io.dfjx.common.synchrodata.Dom4jUtil;
import io.dfjx.common.synchrodata.PortalFilter;
import io.dfjx.common.synchrodata.SynchronizedDataConstants;
import io.dfjx.common.synchrodata.WebClient;
import io.dfjx.modules.etl.service.JobLogService;
import io.dfjx.modules.etl.service.JobService;
import io.dfjx.modules.etl.service.ScriptService;
import io.dfjx.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

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

	@Autowired
	private JobService jobService;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private ScriptService scriptService;

	@RequestMapping("modules/{module}/{url}.html")
	public String module(@PathVariable("module") String module, @PathVariable("url") String url){
		return "modules/" + module + "/" + url;
	}

	@GetMapping(value = {"/", "index.html"})
	public String index(HttpServletRequest request){
        PortalFilter sso = new PortalFilter();
        boolean isLogin = sso.doLogin(request);
        if(!isLogin){
            return "redirect:"+systemParams.getPortalUrl();
        }
        return "index";
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
	public String main(Map<String, Object> map){
		Date now = new Date();
		Date preDate = DateUtils.addDays(now, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		int tasks = jobService.getCount(1);
		int dispatchs = jobLogService.getCount(dateFormat.format(preDate));
		int exes = scriptService.getCount();

		map.put("tasks", tasks);
		map.put("dispatchs", dispatchs);
		map.put("exes", exes);
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
}
