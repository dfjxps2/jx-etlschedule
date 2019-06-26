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
import io.dfjx.modules.etl.dao.ScriptDao;
import io.dfjx.modules.etl.entity.ScriptEntity;
import io.dfjx.modules.etl.entity.ServerEntity;
import io.dfjx.modules.etl.service.ScriptService;
import io.dfjx.modules.etl.service.ServerService;


@Service("scriptService")
public class ScriptServiceImpl extends ServiceImpl<ScriptDao, ScriptEntity> implements ScriptService {
    @Autowired
    private ServerService serverService;
    @Autowired
    private ScriptDao scriptDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String filename = (String)params.get("filename");
        String shareflag = (String)params.get("shareflag");
        String username = (String)params.get("username");
        Page<ScriptEntity> page = this.selectPage(
                new Query<ScriptEntity>(params).getPage(),
                new EntityWrapper<ScriptEntity>().like(StringUtils.isNotBlank(filename),"filename", filename)
                    .where(StringUtils.isNotBlank(shareflag), "((Username={0} and ShareFlag='0') or ShareFlag='1')", username)
        );
    	for(ScriptEntity scriptEntity : page.getRecords()){
    		scriptEntity.setEtlServerName(scriptEntity.getEtlServer());
		}
		return new PageUtils(page);
    }
	@Override
	public List<Map<String, Object>> getScripts() {
		
		return scriptDao.getScripts();
	}
}
