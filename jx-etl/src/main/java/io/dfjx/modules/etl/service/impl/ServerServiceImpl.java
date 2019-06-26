package io.dfjx.modules.etl.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;
import io.dfjx.modules.etl.dao.ServerDao;
import io.dfjx.modules.etl.entity.ServerEntity;
import io.dfjx.modules.etl.service.ServerService;


@Service("serverService")
public class ServerServiceImpl extends ServiceImpl<ServerDao, ServerEntity> implements ServerService {
	@Autowired
	private ServerDao serverDao;
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String etlServer = (String)params.get("etlServer");
		Page<ServerEntity> page = this.selectPage(
				new Query<ServerEntity>(params).getPage(),
				new EntityWrapper<ServerEntity>()
				.like(StringUtils.isNotBlank(etlServer),"etl_server", etlServer)
				);
		return new PageUtils(page);
	}

	@Override
	public List<Map<String, Object>> getServices() {
		
		return serverDao.getServices();
	}



}
