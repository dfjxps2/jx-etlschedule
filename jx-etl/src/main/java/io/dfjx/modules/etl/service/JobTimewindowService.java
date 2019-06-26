package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.JobTimewindowEntity;

import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-09 18:05:44
 */
public interface JobTimewindowService extends IService<JobTimewindowEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

