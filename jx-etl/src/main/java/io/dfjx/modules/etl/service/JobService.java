package io.dfjx.modules.etl.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.dfjx.common.utils.PageUtils;
import io.dfjx.modules.etl.entity.ExlJobConfig;
import io.dfjx.modules.etl.entity.ExlJobDependency;
import io.dfjx.modules.etl.entity.JobEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 15:17:47
 */
public interface JobService extends IService<JobEntity> {

	PageUtils queryPage(Map<String, Object> params);

	int updateJobStatus(String etlSystem, String etlJob, String lastTxDate);

	PageUtils getDependencyJobs(Map<String, Object> params);

	void  insertExt(JobEntity job);
	
	void  updateExt(JobEntity job);
	
    PageUtils getAllDependencyJobs(Map<String, Object> params);

    PageUtils getAllDependencyJobs2(Map<String, Object> params);

	PageUtils getAllDependencyJobs3(Map<String, Object> params);

	Map<Integer, List<Map>> getAllDependencyJobs4(Map<String, Object> params);

	Boolean rerunMulti(Map<String, Object> params);

	Boolean rerunSingle(Integer id, String lastTxDate);

	int updateBatchJobStatus(String[] ids, String newJobStatus);

	Boolean updateSingleJobStatus(int id, String newJobStatus);

	int updateBatchJobTxDate(String[] ids, String newJobTxDate);

	Boolean updateSingleJobTxDate(int id, String newJobTxDate);

	int updateBatchJobEnableFlag(String[] ids, String newEnableFlag);

	Boolean updateSingleJobEnableFlag(int id, String newEnableFlag);

	String jobBatcheConfig() throws Exception;

	List<ExlJobConfig> expJobConfig();

	List<ExlJobDependency> expJobDependency();

	String expJobConfigFile();
}

