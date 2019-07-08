package io.dfjx.modules.etl.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.JobLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-25 15:22:45
 */
public interface JobLogService extends IService<JobLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String getLogDir(String etlSystem, Integer jobsessionid, String scriptfile, String txdate);

    String getLastLogPath(String baseDir, String etlSystem, String etlJob);

    int getCount(String txdate);

    /**
     * 日期分布图 取最近天的数据
     * @param fromDate
     * @param toDate
     * @return
     */
    Map queryLineChartData(String fromDate, String toDate);

    Map queryPieChartData(String fromDate, String toDate);
}

