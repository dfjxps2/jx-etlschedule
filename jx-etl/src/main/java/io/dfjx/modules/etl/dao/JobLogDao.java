package io.dfjx.modules.etl.dao;

import io.dfjx.modules.etl.entity.JobLogEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-25 15:22:45
 */
public interface JobLogDao extends BaseMapper<JobLogEntity> {

    List queryLineChartData(String fromDate, String toDate);

    List queryPieChartData(String fromDate, String toDate);
}
