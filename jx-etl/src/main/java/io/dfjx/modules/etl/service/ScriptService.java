package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.ScriptEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 14:38:56
 */
public interface ScriptService extends IService<ScriptEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public List<Map<String,Object>> getScripts();
}

