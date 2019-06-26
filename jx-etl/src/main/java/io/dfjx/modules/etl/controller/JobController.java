package io.dfjx.modules.etl.controller;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.dfjx.modules.etl.util.DownLoadFileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjx.common.config.SystemParams;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;
import io.dfjx.common.validator.ValidatorUtils;
import io.dfjx.modules.etl.entity.JobEntity;
import io.dfjx.modules.etl.entity.JobStepEntity;
import io.dfjx.modules.etl.service.JobService;
import io.dfjx.modules.etl.service.JobStatusTypeService;
import io.dfjx.modules.etl.service.JobStepService;
import io.dfjx.modules.etl.util.CreateFileUtil;
import io.dfjx.modules.etl.vo.EtlJobStatusTypeVO;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 15:17:47
 */
@RestController
@RequestMapping("etl/job")
public class JobController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JobService jobService;
	@Autowired
	private JobStepService jobStepService;

	@Autowired
	private JobStatusTypeService jobStatusTypeService;

	@Autowired
	private SystemParams systemParams;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("etl:job:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page;
		if (params.containsKey("reqDenpsType") && "true".equals(params.get("reqDenpsType")) && ("false".equals(params.get("reqAllDeps")) || params.get("reqAllDeps")==null )) {
			System.out.println("进入作业单层依赖查询方法");
			page = jobService.getDependencyJobs(params);
		} else if (params.containsKey("reqDenpsType") && "true".equals(params.get("reqDenpsType")) && "true".equals(params.get("reqAllDeps"))){
			System.out.println("进入作业所有依赖查询方法");
			page = jobService.getAllDependencyJobs3(params);
		} else {
			System.out.println("进入作业普通查询方法");
			page = jobService.queryPage(params);
		}

		return R.ok().put("page", page);
	}



	/**
	 * 信息
	 */
	@RequestMapping("/info/{etlSystem}")
	@RequiresPermissions("etl:job:info")
	public R info(@PathVariable("etlSystem") String etlSystem) {
		JobEntity job = jobService.selectById(etlSystem);
		//加载触发作业
		Map<String,Object> map=new HashMap<String,Object>();	
		map.put("ETL_System", job.getEtlSystem());
		map.put("ETL_Job", job.getEtlJob());
		JobStepEntity jse= new JobStepEntity();
		List<JobStepEntity>  listJobs=jobStepService.selectByMap(map);
		if(listJobs.size()>0){
			jse=listJobs.get(0);
			job.setPublicScript(jse.getScriptid());
		}
		return R.ok().put("job", job);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("etl:job:save")
	public R save(@RequestBody JobEntity job) {
		jobService.insertExt(job);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("etl:job:update")
	public R update(@RequestBody JobEntity job) {
		ValidatorUtils.validateEntity(job);
		jobService.updateExt(job);//全部更新
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("etl:job:delete")
	public R delete(@RequestBody String[] etlSystems) {
		jobService.deleteBatchIds(Arrays.asList(etlSystems));

		return R.ok();
	}

	/**
	 * 重跑作业
	 *
	 * @RequestParam
	 */
	@RequestMapping("/rerun")
	@RequiresPermissions("etl:job:rerun")
	public R rerun(@RequestParam Map<String, Object> params) {

		System.out.println("rerun-params==========" + params.toString());
		if (params.containsKey("etlSystem") == false ||
				params.containsKey("etlJob") == false ||
				params.containsKey("lastTxDate") == false) {
			return R.error("作业重跑参数有空值");
		}
		String etlSystem = params.get("etlSystem").toString();
		String etlJob = params.get("etlJob").toString();
		String lastTxDate = params.get("lastTxDate").toString();
		//更新作业状态为Ready
		jobService.updateJobStatus(etlSystem, etlJob, lastTxDate);

		//创建作业触发空文件
		String jobNameUpper = etlJob.toUpperCase();
		String[] lastTxDateArray = lastTxDate.split("-");
		String lastTxDate_new = lastTxDateArray[0] + lastTxDateArray[1] + lastTxDateArray[2];
//		String filename = "/home/etl/ETLAuto/DATA/receive/dir."+ jobNameUpper+lastTxDate_new;
//		String filename = "D://dir." + jobNameUpper + lastTxDate_new;
		String filename = systemParams.getControlFileDir() + "dir." + jobNameUpper + lastTxDate_new;
		if (CreateFileUtil.createFile(filename)) {
			return R.ok("作业成功吊起重跑");
		}
		return R.error("重跑作业【" + etlSystem + "-" + etlJob + "-" + lastTxDate_new + "】失败");
	}


	/**
	 * 获取所有的作业状态
	 */
	@RequestMapping("/getstatus")
	@RequiresPermissions("etl:job:list")
	public R getstatus() {
		List<EtlJobStatusTypeVO> allstatus = jobStatusTypeService.getStatusMap();
		return R.ok().put("allstatus",allstatus);
	}


	/**
	 * 判断上传脚本是否存在
	 * @param
	 * @param
	 * @return
	 */

	@RequestMapping("/fileIsExists/{fileName}") 
	@RequiresPermissions("etl:job:fileIsExists")
	public R fileIsExists(@PathVariable("fileName") String fileName){
		fileName=fileName.toUpperCase();
		String filePath= systemParams.getPublicScriptUploadDir();
		filePath=filePath+fileName;
		File targetFile = new File(filePath);  
		if(targetFile.exists()){    
			return R.error(1, "作业脚本已存在");
		}  
		return R.ok();
	}

	/**
	 * 判断上传作业是否存在
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/jobIsExists") 
	@RequiresPermissions("etl:job:jobIsExists")
	public R jobIsExists(@RequestBody JobEntity job){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ETL_Job", job.getEtlJob());
		List<JobEntity> list=jobService.selectByMap(map);
		if(list.size()>0 && StringUtils.isBlank(job.getId()+"")){    
			return R.error(1, "作业名称已存在");
		} 
		else if(list.size()>0 && StringUtils.isNotBlank(job.getId()+"")){   
			if(list.get(0).getId()!=job.getId()){
				return R.error(1, "作业名称已存在");
			}
		} 
		return R.ok();
	}

	/**
	 * 列表
	 */
	@RequestMapping("/analysis")
	@RequiresPermissions("etl:job:analysis")
	public R analysis(@RequestParam Map<String, Object> params) {
		Map<Integer, List<Map>> data = jobService.getAllDependencyJobs4(params);
		return R.ok().put("data", data);
	}

	/**
	 * 重跑多个作业
	 */

	@RequestMapping("/rerunmulti")
	@RequiresPermissions("etl:job:rerunmulti")
	public R rerunMulti(@RequestParam Map<String, Object> params) {
		System.out.println("rerunmulti params==="+params);
		if(!params.containsKey("rerunjobids") || !params.containsKey("lastTxDate")){
			return R.error("请求参数不正确!");

		}
		jobService.rerunMulti(params);
		return R.ok("批量吊起作业成功");
	}


	/**
	 * 批量更新作业状态
	 */
	@RequestMapping("/upbatchstatus")
	@RequiresPermissions("etl:job:upbatchstatus")
	public R updateBatchStatus(@RequestParam Map<String, Object> params) {
		System.out.println("进入批量更新作业状态方法updateBatchStatus params==="+params);
		if(!params.containsKey("upids") || !params.containsKey("newJobStatus")){
			return R.error("请求参数不正确!");
		}
		String[] ids = params.get("upids").toString().split(",");
		String newJobStatus = params.get("newJobStatus").toString();

		int count = jobService.updateBatchJobStatus(ids,newJobStatus);
		if(count == ids.length){
			return R.ok("批量更新作业状态成功");
		}
		return R.ok("批量更新作业状态失败");
	}

	/**
	 * 批量更新作业数据日期
	 */
	@RequestMapping("/upbatchtxdate")
	@RequiresPermissions("etl:job:upbatchtxdate")
	public R updateBatchJobTxDate(@RequestParam Map<String, Object> params) {
		System.out.println("进入批量更新作业数据日期方法 updateBatchJobTxDate params==="+params);
		if(!params.containsKey("upids") || !params.containsKey("newJobTxdate")){
			return R.error("请求参数不正确!");
		}
		String[] ids = params.get("upids").toString().split(",");
		String newJobTxdate = params.get("newJobTxdate").toString();

		int count = jobService.updateBatchJobTxDate(ids,newJobTxdate);
		if(count == ids.length){
			return R.ok("批量更新作业数据日期成功");
		}
		return R.ok("批量更新作业数据日期失败");
	}

	/**
	 * 批量更新作业有效性
	 */
	@RequestMapping("/upbatchenable")
	@RequiresPermissions("etl:job:upbatchenable")
	public R updateBatchJobEnableFlag(@RequestParam Map<String, Object> params) {
		if(!params.containsKey("upids") || !params.containsKey("newEnableFlag")){
			return R.error("请求参数不正确!");
		}
		String[] ids = params.get("upids").toString().split(",");
		String newJobTxdate = params.get("newEnableFlag").toString();

		int count = jobService.updateBatchJobEnableFlag(ids,newJobTxdate);
		if(count == ids.length){
			return R.ok("批量更新作业有效性成功");
		}
		return R.ok("批量更新作业有效性失败");
	}

	/**
	 * 作业配置excel上传
	 */
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		System.out.println("开始进入文件上传方法");
		if (!file.isEmpty()) {
			String saveFileName = file.getOriginalFilename();
			String fileUploadDir =  systemParams.getPublicScriptUploadDir();
			File saveFile = new File(fileUploadDir + saveFileName);


			if (!saveFile.getParentFile().exists()) {
				return R.error("服务器端错误：上传目录不存在");
			}

			if (saveFile.exists()) {
				return R.error("上传文件已存在");
			}
			try {
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
				out.write(file.getBytes());
				out.flush();
				out.close();
				return R.ok("上传成功");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return R.error("本地上传文件不存在");
			} catch (IOException e) {
				e.printStackTrace();
				return R.error("上传失败");
			}

		} else {
			return R.error("上传文件为空");
		}

	}

	/**
	 * 批量配置作业
	 */
	@RequestMapping("/batchconfig")
	@RequiresPermissions("etl:job:batchconfig")
	public R jobBatchConfig(@RequestParam Map<String, Object> params) {
		String msg;
		try {
			jobService.jobBatcheConfig();
		} catch (Exception e) {
			msg = e.getMessage();
			switch (msg){
				case "err101": return R.error("jobconfig 和 jobdenpency 配置作业不一致,请检查");
				case "err102": return R.error("etl_job 表已存在部分作业,请检查");
				case "err103": return R.error("etl_job_dependency 表已存在部分作业,请检查");
				case "err104": return R.error("etl_job_stream 表已存在部分作业,请检查");
				case "err105": return R.error("etl_job_step 表已存在部分作业,请检查");
				case "err106": return R.error("etl_job_source 表已存在部分作业,请检查");
				case "err107": return R.error("etl_job_timewindow 表已存在部分作业,请检查");
				case "err201": return R.error("etl_job表配置失败,请检查");
				case "err202": return R.error("etl_job_dependency表配置失败,请检查");
				case "err203": return R.error("etl_job_stream表配置失败,请检查");
				case "err204": return R.error("etl_job_step表配置失败,请检查");
				case "err205": return R.error("etl_job_source表配置失败,请检查");
				case "err206": return R.error("etl_job_timewindow表配置失败,请检查");
				case "err207": return R.error("创建作业软连接失败,请检查");
			}

		}


		return R.ok("批量配置作业成功");
	}


	/**
	 * 批量导出作业
	 */
	@RequestMapping("/expconfig")
	@RequiresPermissions("etl:job:expconfig")
	public R jobExpConfig(@RequestParam Map<String, Object> params) {
		String fileName = jobService.expJobConfigFile();
		if(fileName != "error"){
			return R.ok(fileName.trim());
		}
			return R.error("作业配置导出失败");
	}



	/**
	 * 下载导出作业EXCEL
	 */
	@RequestMapping("/dloadconfig")
	@RequiresPermissions("etl:job:dloadconfig")
	public void jobDldConfig(HttpServletRequest request, HttpServletResponse response) {
		String cfgName = request.getParameter("cfgName");
		String filePath = systemParams.getConfigExportDir();
		String fileName = cfgName.trim();
		DownLoadFileUtil.downloadFile(response,filePath,fileName);

	}
}
