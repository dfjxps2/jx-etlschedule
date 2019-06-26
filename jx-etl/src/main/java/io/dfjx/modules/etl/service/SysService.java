package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.SysEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-23 22:44:03
 */
public interface SysService extends IService<SysEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<String> getEtlSys();
}

