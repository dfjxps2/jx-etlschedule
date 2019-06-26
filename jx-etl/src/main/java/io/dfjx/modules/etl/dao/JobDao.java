package io.dfjx.modules.etl.dao;

import io.dfjx.modules.etl.entity.JobEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 15:17:47
 */
@Component
public interface JobDao extends BaseMapper<JobEntity> {

    int updateJobStatus(@Param("etlSystem") String etlSystem,
                        @Param("etlJob") String etlJob,
                        @Param("lastTxDate")String lastTxDate);


    int updateSingleJobStatus(@Param("newJobStatus") String newJobStatus,
                              @Param("id") int id);

    int updateSingleJobTxDate(@Param("newJobTxDate") String newJobStatus,
                              @Param("id") int id);

    int updateSingleJobEnable(@Param("newEnableFlag") String newEnableFlag,
                              @Param("id") int id);
}
