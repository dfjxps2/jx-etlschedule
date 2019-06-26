package io.dfjx.modules.etl.dao;

import io.dfjx.modules.etl.entity.JobDependencyEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-27 10:52:12
 */
@Component
public interface JobDependencyDao extends BaseMapper<JobDependencyEntity> {

    List<JobDependencyEntity> selectDependencyJobs(@Param("etlSystem") String etlSystem,
                                                   @Param("etlJob") String etlJob);

    /**
     * 查询所有依赖
     * @return
     */
    List<Map> selectDependencyAllJobs();
}
