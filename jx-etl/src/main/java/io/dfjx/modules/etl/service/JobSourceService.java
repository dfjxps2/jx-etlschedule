package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.JobSourceEntity;

import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-07 15:37:39
 */
public interface JobSourceService extends IService<JobSourceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

