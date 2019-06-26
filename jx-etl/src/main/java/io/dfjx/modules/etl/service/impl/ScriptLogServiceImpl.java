package io.dfjx.modules.etl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;
import io.dfjx.modules.etl.dao.ScriptDao;
import io.dfjx.modules.etl.dao.ScriptLogDao;
import io.dfjx.modules.etl.entity.ScriptEntity;
import io.dfjx.modules.etl.entity.ScriptLogEntity;
import io.dfjx.modules.etl.service.ScriptLogService;
import io.dfjx.modules.etl.service.ScriptService;
import io.dfjx.modules.etl.service.ServerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("scriptLogService")
public class ScriptLogServiceImpl extends ServiceImpl<ScriptLogDao, ScriptLogEntity> implements ScriptLogService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String filename = (String)params.get("filename");
        String scriptid = (String)params.get("scriptid");
        Page<ScriptLogEntity> page = this.selectPage(
                new Query<ScriptLogEntity>(params).getPage(),
                new EntityWrapper<ScriptLogEntity>().like(StringUtils.isNotBlank(filename),"filename", filename)
                    .eq(StringUtils.isNotBlank(scriptid),"scriptid",scriptid)
        );
        for(ScriptLogEntity scriptLogEntity : page.getRecords()){
            scriptLogEntity.setEtlServerName(scriptLogEntity.getEtlServer());
        }
        return new PageUtils(page);
    }

    @Override
    public Integer addLog(ScriptEntity script, String version,String actions, String message, String author, Date logDate, String versionfile) {
        ScriptLogEntity log = new ScriptLogEntity();
        log.setScriptid(script.getScriptid());
        log.setEtlServer(script.getEtlServer());
        log.setFilename(script.getFilename());
        log.setFilepath(script.getFilepath());
        log.setScripttype(script.getScripttype());
        log.setDescription(script.getDescription());
        log.setScriptversion(version);
        log.setActions(actions);
        log.setAuthor(author);
        log.setMessage(message);
        log.setLogdate(logDate);
        log.setVersionfile(versionfile);
        return baseMapper.insert(log);
    }
}
