package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.JobStatusTypeEntity;
import io.dfjx.modules.etl.vo.EtlJobStatusTypeVO;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-29 12:57:36
 */
public interface JobStatusTypeService extends IService<JobStatusTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<EtlJobStatusTypeVO> getStatusMap();
}

