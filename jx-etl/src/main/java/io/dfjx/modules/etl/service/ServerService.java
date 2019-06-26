package io.dfjx.modules.etl.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.ServerEntity;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-23 21:54:15
 */
public interface ServerService extends IService<ServerEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    
    public List<Map<String,Object>> getServices();
}

