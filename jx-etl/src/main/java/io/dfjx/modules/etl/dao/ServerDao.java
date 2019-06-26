package io.dfjx.modules.etl.dao;

import io.dfjx.modules.etl.entity.ServerEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import net.sf.jsqlparser.schema.Server;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-23 21:54:15
 */
public interface ServerDao extends BaseMapper<ServerEntity> {
	public List<Map<String,Object>> getServices();
}
