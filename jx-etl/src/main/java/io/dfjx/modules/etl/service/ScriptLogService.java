package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.ScriptEntity;
import io.dfjx.modules.etl.entity.ScriptLogEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 14:38:56
 */
public interface ScriptLogService extends IService<ScriptLogEntity> {
    PageUtils queryPage(Map<String, Object> params);

    Integer addLog(ScriptEntity script, String version,String actions, String message, String author, Date logDate, String versionfile);
}

