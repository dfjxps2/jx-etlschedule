package io.dfjx.modules.etl.dao;

import io.dfjx.modules.etl.entity.ScriptEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 14:38:56
 */
public interface ScriptDao extends BaseMapper<ScriptEntity> {
	public List<Map<String,Object>> getScripts();
	
}
