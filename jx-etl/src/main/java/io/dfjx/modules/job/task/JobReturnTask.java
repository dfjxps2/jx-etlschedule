package io.dfjx.modules.job.task;

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

import com.google.gson.Gson;
import io.dfjx.common.utils.DateUtils;
import io.dfjx.modules.etl.dto.RerunMultiDto;
import io.dfjx.modules.etl.service.JobService;
import io.dfjx.modules.sys.entity.SysUserEntity;
import io.dfjx.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 重跑任务
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.2.0 2016-11-28
 */
@Component("jobReturnTask")
public class JobReturnTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JobService jobService;

	public void rerunMulti(String params){
		logger.info("我是带参数的rerunMulti方法，正在被执行，参数为：" + params);

		RerunMultiDto rerunMultiDto = new Gson().fromJson(params, RerunMultiDto.class);
		Date date = DateUtils.addDateDays(new Date(), rerunMultiDto.getLastTxDate());
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("rerunjobids", rerunMultiDto.getRerunjobids());
		paramsMap.put("lastTxDate", DateUtils.format(date, "yyyy-MM-dd"));
		jobService.rerunMulti(paramsMap);

	}

}
