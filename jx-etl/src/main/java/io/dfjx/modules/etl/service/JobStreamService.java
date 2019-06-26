package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.JobStreamEntity;

import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-07 10:42:51
 */
public interface JobStreamService extends IService<JobStreamEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

