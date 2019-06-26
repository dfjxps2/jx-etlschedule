package io.dfjx.modules.etl.dao;

import io.dfjx.modules.etl.entity.SysEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-23 22:44:03
 */
public interface SysDao extends BaseMapper<SysEntity> {

    List<String> selectEtlSys();
}
