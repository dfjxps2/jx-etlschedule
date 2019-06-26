package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.JobDependencyEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-27 10:52:12
 */
public interface JobDependencyService extends IService<JobDependencyEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<JobDependencyEntity> getDependencyJobs(String etlSystem, String etlJob);

    List<Map> getDependencyAllJobs();
}

