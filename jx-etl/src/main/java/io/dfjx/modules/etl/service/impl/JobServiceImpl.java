package io.dfjx.modules.etl.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import io.dfjx.modules.etl.entity.*;
import io.dfjx.modules.etl.service.*;
import io.dfjx.modules.etl.util.CreateFileUtil;
import io.dfjx.modules.etl.util.DownLoadFileUtil;
import io.dfjx.modules.etl.util.ExcelData;
import io.dfjx.modules.etl.util.ExcelTool;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.dfjx.common.config.SystemParams;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;
import io.dfjx.modules.etl.dao.JobDao;
import io.dfjx.modules.etl.dao.JobDependencyDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service("jobService")
public class JobServiceImpl extends ServiceImpl<JobDao, JobEntity> implements JobService {

	@Autowired
	private JobDao jobDao;


	@Autowired
	private JobDependencyDao jobDependencyDao;

	@Autowired
	private JobStreamService jobStreamService;

    @Autowired
    private JobDependencyService jobDependencyService;

	@Autowired
	private JobStepService jobStepService;

	@Autowired
	private JobSourceService jobSourceService;	
	
	@Autowired
	private JobTimewindowService jobTimewindowService;	
	
	@Autowired
	private SystemParams systemParams;

	@Autowired
	private ScriptService scriptService;


	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<JobEntity> page = new Page<JobEntity>();
		System.out.println("===================" + params.toString());
		if(params.containsKey("etlJob")){
			String etlJob = params.get("etlJob")!=null? params.get("etlJob").toString().toLowerCase():null;
			String etlSystem = params.get("etlSystem")!=null? params.get("etlSystem").toString():null;
			String lastTxDateStart = params.get("lastTxDateStart")!=null? params.get("lastTxDateStart").toString():null;
			String lastTxDateEnd = params.get("lastTxDateEnd")!=null? params.get("lastTxDateEnd").toString():null;
			String lastJobStatus = params.get("lastJobStatus")!=null? params.get("lastJobStatus").toString():null;
			String enable = params.get("enable")!=null? params.get("enable").toString():null;

			String etlJobNow = params.get("etlJobNow")!=null? params.get("etlJobNow").toString():null;


			StringBuffer sql=new StringBuffer();
			if(StringUtils.isNotBlank(etlJob)){
				sql.append(" and etl_Job like '%" + etlJob +"%' ");
			}
			if(StringUtils.isNotBlank(etlSystem)){
				sql.append(" and etl_System like '%"+ etlSystem +"%' ");
			}
			if(StringUtils.isNotBlank(lastTxDateStart)){
				sql.append(" and (last_TxDate>='" + lastTxDateStart +"' or last_TxDate is null) ");
			}
			if(StringUtils.isNotBlank(lastTxDateEnd)){
				sql.append(" and (last_TxDate<='" + lastTxDateEnd +"' or last_TxDate is null) ");
			}
			if(StringUtils.isNotBlank(lastJobStatus)){
				sql.append(" and last_JobStatus like '%" + lastJobStatus +"%' ");
			}	
			if(StringUtils.isNotBlank(enable)){
				sql.append(" and Enable ='" + enable +"' ");
			}
			if(StringUtils.isNotBlank(etlJobNow)){
				sql.append(" and etl_Job <>'" + etlJobNow +"' ");
			}

			page = this.selectPage(
					new Query<JobEntity>(params).getPage(),
					new EntityWrapper<JobEntity>()
					//                            .like(StringUtils.isNotBlank(etlJob),"etl_Job", etlJob)
					//                            .like(StringUtils.isNotBlank(etl_system),"etl_system",etlSystem)
					//                            .eq("last_TxDate",lastTxDate)
					.where(" 1=1 "+sql.toString() ));
		} else {
			page = this.selectPage(
					new Query<JobEntity>(params).getPage(),
					new EntityWrapper<JobEntity>()
					);
		}



		return new PageUtils(page);
	}

	@Override
	public int updateJobStatus(String etlSystem,String etlJob,String lastTxDate){
		System.out.println("jobserviceimpl.lastTxDate=============="+ lastTxDate);
		return jobDao.updateJobStatus(etlSystem,etlJob,lastTxDate);
	};


	/**
	 * 查找依赖任务
	 * @param params
	 * @return
	 */
	public PageUtils getDependencyJobs(Map<String, Object> params){
		System.out.println("service-params======" + params.toString());
		String etlSystem = params.get("dep_etlSystem").toString();
		String etlJob = params.get("dep_etlJob").toString();
		List<JobDependencyEntity> delist = jobDependencyDao.selectDependencyJobs(etlSystem,etlJob);
		EntityWrapper<JobEntity> jobwarper = new  EntityWrapper<JobEntity>();
		int size = delist.size();
		Page<JobEntity>  page = new Page<JobEntity>();
		//加载依赖作业
		if(size>0){
			for(int i=0;i<size;i++){
				if(i==0){
					jobwarper.where("etl_system='"+delist.get(i).getDependencySystem()+ "' and etl_job='"+delist.get(i).getDependencyJob()+"'");
				}else{
					jobwarper.or("etl_system='"+delist.get(i).getDependencySystem()+ "' and etl_job='"+delist.get(i).getDependencyJob()+"'");
				}
			}

			page = this.selectPage(new Query<JobEntity>(params).getPage(),jobwarper);
		}
		//加载触发作业
		Map<String,Object> map=new HashMap<String,Object>();	
		map.put("Stream_System", etlSystem);
		map.put("Stream_Job", etlJob);
		JobStreamEntity jse= new JobStreamEntity();
		List<JobStreamEntity>  listJobs=jobStreamService.selectByMap(map);
		if(listJobs.size()>0){
			jse=listJobs.get(0);
		}
		for(int i=0;i<page.getRecords().size();i++){
			if(page.getRecords().get(i).getEtlJob().equals(jse.getEtlJob()) && page.getRecords().get(i).getEtlSystem().equals(jse.getEtlSystem()) ){
				page.getRecords().get(i).setIsTriggerJob("1");
				break;
			}
		}
		return  new PageUtils(page);
	}

	@Override
	@Transactional
	public void insertExt(JobEntity job) {
		//保存etl_job
		job.setJobtype("D");
		job.setLastFilecnt(0);
		job.setCubeflag("N");
		job.setAutooff("N");
		job.setCheckcalendar("N");
		job.setJobsessionid(0);
		job.setExpectedrecord(0);
		job.setChecklaststatus("Y");
		job.setTimetrigger("N");
		job.setPriority(0);
		jobDao.insert(job);
		saveDependencyJob(job);
		saveTriggerJob(job);
		saveJobStep(job);
		saveJobSource(job);
		saveTimewindow(job);
		if(systemParams.getCreateLnFlag()=="true") {
			createLnFile(job);
		}
	}
	@Override
	@Transactional
	public void updateExt(JobEntity job) {
		//System.out.println(job);
		jobDao.updateById(job);
		saveDependencyJob(job);
		saveTriggerJob(job);
		saveJobStep(job);
		saveJobSource(job);
		saveTimewindow(job);
		if(systemParams.getCreateLnFlag()=="true") {
			createLnFile(job);
		}
	}
	//保存依赖作业

	private void saveDependencyJob(JobEntity job){
		if(job.getAllDependSave()==null || job.getAllDependSave().length==0){
			return ;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		List<JobEntity>  dependLists=jobDao.selectBatchIds(Arrays.asList(job.getAllDependSave()));
		map.put("ETL_System",job.getEtlSystem() );
		map.put("ETL_Job", job.getEtlJob());
		jobDependencyDao.deleteByMap(map);
		JobDependencyEntity jde = new JobDependencyEntity();
		for(JobEntity j:dependLists){
			jde=new JobDependencyEntity();
			jde.setEtlJob(job.getEtlJob());
			jde.setEtlSystem(job.getEtlSystem());
			jde.setDependencyJob(j.getEtlJob());
			jde.setDependencySystem(j.getEtlSystem());
			jde.setEnable("1");
			jobDependencyDao.insert(jde);
		}
	}
	//保存触发作业

	private void saveTriggerJob(JobEntity job){
		if(StringUtils.isBlank(job.getTriggerJob())){
			return ;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("Stream_System",job.getEtlSystem() );
		map.put("Stream_Job", job.getEtlJob());
		jobStreamService.deleteByMap(map);
		JobEntity job2=jobDao.selectById(job.getTriggerJob());
		JobStreamEntity jse=new JobStreamEntity();
		jse.setEtlJob(job2.getEtlJob());
		jse.setEtlSystem(job2.getEtlSystem());
		jse.setStreamJob(job.getEtlJob());
		jse.setStreamSystem(job.getEtlSystem());
		jse.setEnable("1");
		jobStreamService.insert(jse);
	}

	//保存作业步骤

	private void saveJobStep(JobEntity job){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ETL_System",job.getEtlSystem() );
		map.put("ETL_Job", job.getEtlJob());
		jobStepService.deleteByMap(map);

		JobStepEntity jse=new JobStepEntity();
		jse.setEtlSystem(job.getEtlSystem());
		jse.setEtlJob(job.getEtlJob());
		jse.setJobstepid("0100");
		jse.setScriptid(job.getPublicScript());
		jse.setScriptfile(job.getRunningscript());
		jse.setEnable("1");
		jobStepService.insert(jse);


	}





	//保存作业源

	private void saveJobSource(JobEntity job){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ETL_System",job.getEtlSystem() );
		map.put("ETL_Job", job.getEtlJob());
		jobSourceService.deleteByMap(map);

		JobSourceEntity jse=new JobSourceEntity();
		jse.setEtlSystem(job.getEtlSystem());
		jse.setEtlJob(job.getEtlJob());
		jse.setSource(job.getEtlJob());
		jse.setConvFileHead(job.getEtlJob());
		jse.setAutofilter("0");
		jse.setAlert("0");
		jse.setBeforehour(0);
		jse.setBeforemin(0);
		jse.setOffsetday(0);

		jobSourceService.insert(jse);
	}

	//保存timewindow

	private void saveTimewindow(JobEntity job){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ETL_System",job.getEtlSystem() );
		map.put("ETL_Job", job.getEtlJob());
		jobTimewindowService.deleteByMap(map);
		JobTimewindowEntity jtw=new JobTimewindowEntity();
		jtw.setEtlSystem(job.getEtlSystem());
		jtw.setEtlJob(job.getEtlJob());
		jtw.setAllow("Y");
		jtw.setBeginhour(0);
		jtw.setEndhour(23);
		jobTimewindowService.insert(jtw);
	}


	//创建软链接
	private Boolean createLnFile(JobEntity job){
		//软链接创建根目录
		String APPDir = systemParams.getExecFileLnDir();
		//软连接目录
		String LnDir = APPDir + job.getEtlSystem() + "/" + job.getEtlJob() + "/bin/";
		//执行脚本
		String execFile = job.getRunningscript();
		//公共脚本
		String pubScriptFile = systemParams.getPublicScriptUploadDir()+scriptService.selectById(job.getPublicScript()).getFilename();
		//创建软链接命令
		String createLnCmd = "ln -s  " + pubScriptFile + "  "+ LnDir+ execFile;
		//删除软链接命令
		String rmLnCmd = "rm -f " + LnDir+ execFile;

		Runtime runtime = Runtime.getRuntime();
		//判断作业系统和作业名称目录是否存在,不存在则创建目录后同时创建软链接，若存在则删除软链接再重建软链接
		File lnDirF = new File(LnDir);
		if(lnDirF.exists()){
			try {
//				String allCmd = rmLnCmd + " && " + createLnCmd;
//				Process process = runtime.exec(allCmd);
//				process.waitFor();
                Process process = runtime.exec(rmLnCmd);
                process.waitFor();
                Process process1 = runtime.exec(createLnCmd);
                process1.waitFor();

				return true;
			} catch (IOException e) {
				e.printStackTrace();
                return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
                return false;
			}
		}else{

			try {
				lnDirF.mkdirs();
				Process process = runtime.exec(createLnCmd);
				process.waitFor();
                return true;
			} catch (IOException e) {
				e.printStackTrace();
                return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
                return false;
			}
		}
	}



	/**
	 * 查找所有依赖任务递归方法
	 * @param
	 * @return
	 */
	public  List<String> getAllDependencyJobsFunc(String etlSystem,String etlJob,List<String> list) {
		List<String> tmplist = new ArrayList<String>();
		List<JobDependencyEntity> mylist = jobDependencyDao.selectDependencyJobs(etlSystem, etlJob);
		for (JobDependencyEntity jobDependencyEntity : mylist) {
			String str = jobDependencyEntity.getDependencySystem() + "," + jobDependencyEntity.getDependencyJob();
			tmplist.add(str);
		}
		if (tmplist.size() == 0) {
			return list;
		} else {
			list.addAll(tmplist);
			for (String str : tmplist) {
                getAllDependencyJobsFunc(str.split(",")[0], str.split(",")[1], list);
			}
		}

		return list;
	}

    /**
     * 查找所有依赖任务
     * @param params
     * @return
     */
    public PageUtils getAllDependencyJobs(Map<String, Object> params){
        System.out.println("getAllDependencyJobs-params======" + params.toString());
        String etlSystem = params.get("dep_etlSystem").toString();
        String etlJob = params.get("dep_etlJob").toString();
        List<String> deplist = new ArrayList<String>();
        deplist = this.getAllDependencyJobsFunc(etlSystem,etlJob,deplist);
        EntityWrapper<JobEntity> jobwarper = new  EntityWrapper<JobEntity>();
        int size = deplist.size();
        Page<JobEntity>  page = new Page<JobEntity>();

        if(size>0){
            for(int i=0;i<size;i++){
                if(i==0){
                    jobwarper.where("etl_system='"+deplist.get(i).split(",")[0]+ "' and etl_job='"+deplist.get(i).split(",")[1]+"'");
                }else{
                    jobwarper.or("etl_system='"+deplist.get(i).split(",")[0]+ "' and etl_job='"+deplist.get(i).split(",")[1]+"'");
                }
            }

            page = this.selectPage(new Query<JobEntity>(params).getPage(),jobwarper);
        }else{

        }
        return  new PageUtils(page);

    }



	/**
	 * 查找所有依赖任务递归方法2-带依赖层级
	 * @param
	 * @return
	 */
	public  Map<Integer,List<String>> getAllDependencyJobsFunc2(Integer level,String etlSystem,String etlJob,Map<Integer,List<String>> list) {

		System.out.println("level=============" + level);

		if(level<1){
			return list;
		}

		List<String> tmplist = new ArrayList<String>();

		List<JobDependencyEntity> mylist = jobDependencyDao.selectDependencyJobs(etlSystem, etlJob);

		for (JobDependencyEntity jobDependencyEntity : mylist) {
			String str = jobDependencyEntity.getDependencySystem() + "," + jobDependencyEntity.getDependencyJob();
			tmplist.add(str);
		}

		if (tmplist.size() == 0) {
			return list;
		} else {

			if(list.containsKey(level)){
				tmplist.addAll(list.get(level));
			}
			list.put(level,tmplist);
			level = level-1;
			for (String str : tmplist) {
				getAllDependencyJobsFunc2(level,str.split(",")[0], str.split(",")[1], list);
			}
		}

		return list;
	}



	/**
	 * 查找所有依赖任务-带依赖层级
	 * @param params
	 * @return
	 */
	public PageUtils getAllDependencyJobs2(Map<String, Object> params){
		System.out.println("getAllDependencyJobs2-params======" + params.toString());
		String etlSystem = params.get("dep_etlSystem").toString();
		String etlJob = params.get("dep_etlJob").toString();
		Integer level;
		if(!params.containsKey("level")){
			 level = 2;
		} else {
			 level = Integer.parseInt(params.get("level").toString());
		}


		Map<Integer,List<String>> deplist = new HashMap<Integer,List<String>>();
		deplist = this.getAllDependencyJobsFunc2(level,etlSystem,etlJob,deplist);

		int size = deplist.keySet().size();
		Page<JobEntity>  page = new Page<JobEntity>();
		List<JobEntity> listentitys = new ArrayList<JobEntity>();

//		while (size>0 && deplist.get(size) != null){
		while (size>0){
			List<String>  tmplist = deplist.get(size);
			System.out.println("tmplist===========" + tmplist.toString());
			for(String str : tmplist){
				String etlSystemTmp = str.split(",")[0];
				String etlJobTmp = str.split(",")[1];
				JobEntity jobEntity = jobDao.selectList(new EntityWrapper<JobEntity>()
										.eq("etl_System",etlSystemTmp)
										.eq("etl_Job",etlJobTmp)
				).get(0);
				jobEntity.setLastFilecnt(size);
				listentitys.add(jobEntity);
			}
			size = size-1;
		}
		page.setRecords(listentitys);
		return  new PageUtils(page);

	}


	private String putStr(Map<String, Object> map, String key){
		Object obj = map.get(key);
		if(obj == null)
			return "";
		return obj.toString();
	}
	/**
	 * 查找所有依赖任务递归方法2
	 * @param
	 * @return
	 */
	private void findAllDependencyJobs(String etlSystem,String etlJob, List<Map> alllist,List<String> list){
		List<String[]> tmplist = new ArrayList<String[]>();
		if(etlSystem == null || etlJob == null)
			return;
		for (Map map : alllist) {
			String sys = putStr(map, "etlSystem");
			String job = putStr(map, "etlJob");
			if(etlSystem.equals(sys) && etlJob.equals(job)){
				String dependencySystem = putStr(map, "dependencySystem");
				String dependencyJob = putStr(map, "dependencyJob");
				if(dependencySystem.length() == 0 || dependencyJob.length() == 0)
					continue;
				String[] strs = new String[]{dependencySystem,  dependencyJob};
				tmplist.add(strs);
				String id = map.get("id").toString();
				if(!list.contains(id))
					list.add(id);
			}
		}

		if (tmplist.size() == 0) {
			return;
		} else {
			for (String[] arr : tmplist) {
				findAllDependencyJobs(arr[0], arr[1], alllist, list);
			}
		}
	}

	/**
	 * 查找所有依赖任务-使用内存
	 * @param params
	 * @return
	 */
	public PageUtils getAllDependencyJobs3(Map<String, Object> params){
		String etlSystem = params.get("dep_etlSystem").toString();
		String etlJob = params.get("dep_etlJob").toString();
		List<String> deplist = new ArrayList<>();

		List<Map> dependencylist = jobDependencyDao.selectDependencyAllJobs();
		findAllDependencyJobs(etlSystem, etlJob, dependencylist, deplist);
		EntityWrapper<JobEntity> jobwarper = new  EntityWrapper<>();
		int size = deplist.size();
		Page<JobEntity>  page = new Page<>();

		if(size>0){
			jobwarper.in("id", deplist);
			page = this.selectPage(new Query<JobEntity>(params).getPage(),jobwarper);
		}
		return  new PageUtils(page);
	}
	/**
	 * 查找所有依赖任务递归方法2
	 * @param
	 * @return
	 */
	private void findAllDependencyJobs2(String etlSystem,String etlJob, List<Map> alllist, int direct, int lv, List<String> mapids, Map<Integer, List<Map>> maps
			, String sourceCol1, String sourceCol2, String targetCol1, String targetCol2 ){
		List<String[]> tmplist = new ArrayList<String[]>();
		if(etlSystem == null || etlJob == null)
			return;
		List<Map> newlist;
		if(!maps.containsKey(lv)){
			newlist = new ArrayList<>();
			maps.put(lv, newlist);
		}else{
			newlist = maps.get(lv);
		}
		for (Map map : alllist) {
			String sys = putStr(map, sourceCol1);
			String job = putStr(map, sourceCol2);
			if(etlSystem.equals(sys) && etlJob.equals(job)){
				String id = putStr(map, "id");
				if(!mapids.contains(id)){
					newlist.add(map);
					mapids.add(id);

					//find next（已添加节点不再继续）
					String dependencySystem = putStr(map, targetCol1);
					String dependencyJob = putStr(map, targetCol2);
					if(dependencySystem.length() == 0 || dependencyJob.length() == 0)
						continue;
					String[] strs = new String[]{dependencySystem,  dependencyJob};
					tmplist.add(strs);
				}else{
					newlist.add(map);
				}

			}
		}

		if (tmplist.size() == 0) {
			return;
		} else {
			int lv_next = lv + direct;
			for (String[] arr : tmplist) {
				findAllDependencyJobs2(arr[0], arr[1], alllist, direct, lv_next, mapids, maps, sourceCol1, sourceCol2, targetCol1, targetCol2);
			}
		}
	}

	/**
	 * 查找所有依赖和被依赖任务-带依赖层级-使用内存
	 * @param params
	 * @return
	 */
	public Map<Integer, List<Map>> getAllDependencyJobs4(Map<String, Object> params){
		String etlSystem = params.get("dep_etlSystem").toString();
		String etlJob = params.get("dep_etlJob").toString();
		Map<Integer, List<Map>> allmaps = new HashMap<>();
		allmaps.put(0, new ArrayList<>());
		List<String> mapids = new ArrayList<>();

		List<Map> dependencylist = jobDependencyDao.selectDependencyAllJobs();
		//向上查找血缘
		findAllDependencyJobs2(etlSystem, etlJob, dependencylist, -1, -1, mapids, allmaps, "etlSystem", "etlJob", "dependencySystem", "dependencyJob");
		//向下查找影响
		//findAllDependencyJobs2(etlSystem, etlJob, dependencylist, 1, -1, mapids, allmaps, "dependencySystem", "dependencyJob", "etlSystem", "etlJob");
		//添加自身节点
		EntityWrapper<JobEntity> jobwarper = new  EntityWrapper<>();
		jobwarper.where("etl_system='"+etlSystem+ "' and etl_job='"+etlJob+"'");
		Map curr = this.selectMap(jobwarper);
		curr.put("dependencyJob", putStr(curr, "etlJob"));
		curr.put("dependencySystem", putStr(curr, "etlSystem"));
		allmaps.get(0).add(curr);

		return allmaps;
	}

	/**
	 * 按照作业id重跑多个作业
	 */
	public Boolean rerunMulti(Map<String, Object> params){
		String[] rerunjobids = params.get("rerunjobids").toString().split(",");
		String lastTxDate = params.get("lastTxDate").toString();
		for(String s : rerunjobids){
			Integer id = Integer.parseInt(s);
			rerunSingle(id,lastTxDate);
		}

		return true;
	}

	/**
	 * 按照作业id重跑单个作业
	 */
	public Boolean rerunSingle(Integer id,String lastTxDate){
		JobEntity jobEntity = jobDao.selectById(id);
		//更新作业状态为Ready
		updateJobStatus(jobEntity.getEtlSystem(), jobEntity.getEtlJob(), lastTxDate);

		//创建作业触发空文件
		String jobNameUpper = jobEntity.getEtlJob().toUpperCase();
		String[] lastTxDateArray = lastTxDate.split("-");
		String lastTxDate_new = lastTxDateArray[0] + lastTxDateArray[1] + lastTxDateArray[2];
		String filename = systemParams.getControlFileDir() + "dir." + jobNameUpper + lastTxDate_new;
		if (CreateFileUtil.createFile(filename)) {
			return true;
		}

		return false;
	}

	/**
	 * 更新单个作业状态
	 * @param id
	 * @param newJobStatus
	 * @return
	 */
	public Boolean updateSingleJobStatus(int id,String newJobStatus){
		jobDao.updateSingleJobStatus(newJobStatus,id);
		return true;
	}

	public int updateBatchJobStatus(String[] ids,String newJobStatus){
		int count = 0;
		for(String id : ids){
			if (updateSingleJobStatus(Integer.parseInt(id),newJobStatus)){
				count ++;
			}
		}

		return count;
	}


	public int updateBatchJobTxDate(String[] ids,String newJobTxDate){
		int count = 0;
		for(String id : ids){
			if (updateSingleJobTxDate(Integer.parseInt(id),newJobTxDate)){
				count ++;
			}
		}

		return count;
	}

	public Boolean updateSingleJobTxDate(int id,String newJobTxDate){
		jobDao.updateSingleJobTxDate(newJobTxDate,id);
		return true;
	}

	public int updateBatchJobEnableFlag(String[] ids, String newEnableFlag){
		int count = 0;
		for(String id : ids){
			if (updateSingleJobEnableFlag(Integer.parseInt(id),newEnableFlag)){
				count ++;
			}
		}
		return count;
	}

	public Boolean updateSingleJobEnableFlag(int id, String newEnableFlag){
		jobDao.updateSingleJobEnable(newEnableFlag,id);
		return true;
	}


    @Transactional(rollbackFor = Exception.class)
	public String jobBatcheConfig() throws Exception{
		File file = new File(systemParams.getPublicScriptUploadDir()+"job_batch_config.xls");
		List<String[]> jobconfiglist = ExcelData.getSheetDataBySheetName(file,"jobconfig");
		List<String[]> jobdenpencylist = ExcelData.getSheetDataBySheetName(file,"jobdenpency");
        String platform="";
		String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
           platform = "windows";
        } else {
            platform = "linux";
        }

        String returncode = "0";
        if(!check2SheetJobsSizeEquals(jobconfiglist,jobdenpencylist)){
			returncode = "err101";
		} else if (checkJobEntityIsExist(jobconfiglist)){
			returncode = "err102";
        } else if (checkJobDependencyEntityIsExist(jobdenpencylist)){
			returncode = "err103";
        } else if (checkJobStreamEntityIsExist(jobdenpencylist)){
			returncode = "err104";
        } else if(checkJobStepEntityIsExist(jobconfiglist)){
			returncode = "err105";
        } else if (checkJobSourceEntityIsExist(jobconfiglist)){
			returncode = "err106";
        } else if (checkJobTimewindowEntityIsExist(jobconfiglist)){
			returncode = "err107";
        } else if (!installJobEntity(jobconfiglist)){
			returncode = "err201";
        } else if (!installJobDependency(jobdenpencylist)){
			returncode = "err202";
        } else if (!installJobStream(jobdenpencylist)){
			returncode = "err203";
        } else if (!installJobStep(jobconfiglist)){
			returncode = "err204";
        } else if (!installJobSource(jobconfiglist)){
			returncode = "err205";
        } else if (!installJobTimewindow(jobconfiglist)){
			returncode = "err206";
        } else if (systemParams.getCreateLnFlag()=="true" && platform=="linux" && !installJobLink(jobconfiglist)){
			returncode = "err207";
        }
        if(!returncode.equals("0")){
			throw new Exception(returncode);
		}

		return returncode;
	}


    private Boolean installJobEntity(List<String[]> jobconfiglist){
	    int totalCount = jobconfiglist.size();
	    int successCount = 0;
	    Date date = new Date();
        for(String[] strings:jobconfiglist){
            JobEntity jobEntity = new JobEntity();
            jobEntity.setEtlSystem(strings[0].toUpperCase().trim());
            jobEntity.setEtlJob(strings[1].toUpperCase().trim());
            jobEntity.setEtlServer(strings[2].toUpperCase().trim());
            jobEntity.setDescription(strings[3].toUpperCase().trim());
            jobEntity.setFrequency(strings[4].toUpperCase().trim());
            jobEntity.setJobtype("D");
//            jobEntity.setEnable("1");
            jobEntity.setEnable(strings[8].trim());
            jobEntity.setLastJobstatus("Ready");
//            jobEntity.setLastTxdate(date);
			jobEntity.setRunningscript(strings[1].toLowerCase().trim()+"0100."+strings[7].trim());
           	jobEntity.setEtlServer(strings[2].trim());
            jobEntity.setLastFilecnt(0);
            jobEntity.setCubeflag("N");
            jobEntity.setAutooff("N");
            jobEntity.setCheckcalendar("N");
            jobEntity.setJobsessionid(0);
            jobEntity.setExpectedrecord(0);
            jobEntity.setChecklaststatus("Y");
            jobEntity.setTimetrigger("N");
            jobEntity.setPriority(0);
            successCount = successCount + jobDao.insert(jobEntity);
        }

        if(totalCount == successCount){
            return  true;
        }
        return false;
    }

    private Boolean installJobDependency(List<String[]> jobdenpencylist){
        int totalCount = jobdenpencylist.size();
        int successCount = 0;
        for(String[] strings:jobdenpencylist){
            JobDependencyEntity jobDependencyEntity = new JobDependencyEntity();
            jobDependencyEntity.setEtlSystem(strings[0].toUpperCase().trim());
            jobDependencyEntity.setEtlJob(strings[1].toUpperCase().trim());
            jobDependencyEntity.setDependencySystem(strings[2].toUpperCase().trim());
            jobDependencyEntity.setDependencyJob(strings[3].toUpperCase().trim());
//            jobDependencyEntity.setEnable("1");
            jobDependencyEntity.setEnable(strings[5].trim());

            if(jobDependencyService.insert(jobDependencyEntity)){
                successCount++;
            }
        }

        if(totalCount == successCount){
            return  true;
        }
        return false;
    }

    private Boolean installJobStream(List<String[]> jobdenpencylist){
        int totalCount = 0;
        int successCount = 0;
        for(String[] strings:jobdenpencylist){
            if(strings[4].trim().equals("1")) {
                totalCount++;
                JobStreamEntity jobStreamEntity = new JobStreamEntity();
                jobStreamEntity.setEtlSystem(strings[2].toUpperCase().trim());
                jobStreamEntity.setEtlJob(strings[3].toUpperCase().trim());
                jobStreamEntity.setStreamSystem(strings[0].toUpperCase().trim());
                jobStreamEntity.setStreamJob(strings[1].toUpperCase().trim());
                jobStreamEntity.setEnable("1");

                if (jobStreamService.insert(jobStreamEntity)) {
                    successCount++;
                }
            }
        }
        if(totalCount == successCount && successCount != 0){
            return  true;
        }
        return false;
    }

    private Boolean installJobStep(List<String[]> jobconfiglist){
        int totalCount = jobconfiglist.size();
        int successCount = 0;
        for(String[] strings:jobconfiglist){
            JobStepEntity jobStepEntity = new JobStepEntity();
            jobStepEntity.setEtlSystem(strings[0].toUpperCase().trim());
            jobStepEntity.setEtlJob(strings[1].toUpperCase().trim());
            jobStepEntity.setJobstepid("0100");
            jobStepEntity.setScriptid(Integer.parseInt(strings[6]));
            jobStepEntity.setScriptfile(strings[1].toLowerCase()+"0100."+strings[7]);
            jobStepEntity.setEnable("1");

            if(jobStepService.insert(jobStepEntity)){
                successCount++;
            }
        }

        if(totalCount == successCount){
            return  true;
        }
        return false;
    }


    private Boolean installJobSource(List<String[]> jobconfiglist){
        int totalCount = jobconfiglist.size();
        int successCount = 0;
        for(String[] strings:jobconfiglist){
            JobSourceEntity jobSourceEntity = new JobSourceEntity();
            jobSourceEntity.setSource(strings[1].toUpperCase().trim());
            jobSourceEntity.setEtlSystem(strings[0].toUpperCase().trim());
            jobSourceEntity.setEtlJob(strings[1].toUpperCase().trim());
            jobSourceEntity.setConvFileHead(strings[1].toUpperCase().trim());
            jobSourceEntity.setAutofilter("0");
            jobSourceEntity.setAlert("0");
            jobSourceEntity.setBeforehour(0);
            jobSourceEntity.setBeforemin(0);
            jobSourceEntity.setOffsetday(0);

            if(jobSourceService.insert(jobSourceEntity)){
                successCount++;
            }
        }

        if(totalCount == successCount){
            return  true;
        }
        return false;
    }

    private Boolean installJobTimewindow(List<String[]> jobconfiglist){
        int totalCount = jobconfiglist.size();
        int successCount = 0;
        for(String[] strings:jobconfiglist){
            JobTimewindowEntity jobTimewindowEntity = new JobTimewindowEntity();
            jobTimewindowEntity.setEtlSystem(strings[0].toUpperCase().trim());
            jobTimewindowEntity.setEtlJob(strings[1].toUpperCase().trim());
            jobTimewindowEntity.setAllow("Y");
            jobTimewindowEntity.setBeginhour(0);
            jobTimewindowEntity.setEndhour(23);
            if(jobTimewindowService.insert(jobTimewindowEntity)){
                successCount++;
            }
        }

        if(totalCount == successCount){
            return  true;
        }
        return false;
    }


    private Boolean installJobLink(List<String[]> jobconfiglist){
        int totalCount = jobconfiglist.size();
        int successCount = 0;
        for(String[] strings:jobconfiglist){
            JobEntity jobEntity = new JobEntity();
            jobEntity.setEtlSystem(strings[0].toUpperCase().trim());
            jobEntity.setEtlJob(strings[1].toUpperCase().trim());
            jobEntity.setPublicScript(Integer.parseInt(strings[6].trim()));
            jobEntity.setRunningscript(strings[1].toLowerCase().trim()+"0100."+strings[7]);

            if(createLnFile(jobEntity)){
                successCount++;
            }
        }

        if(totalCount == successCount && successCount != 0){
            return  true;
        }
        return false;
    }


	private Boolean check2SheetJobsSizeEquals(List<String[]> list1,List<String[]> list2){
		SortedSet<String> set1 = getEtlJobs(list1);
		SortedSet<String> set2 = getEtlJobs(list2);
		if(set1.size() < set2.size() || set1.size()==0 || set2.size()==0){
			return false;
		}

		Iterator it2 = set2.iterator();
		while(it2.hasNext()){
		    String item = it2.next().toString();
		    if(!set1.contains(item)){
		        return false;
            }
        }

		return true;
	}

	private Boolean checkJobEntityIsExist(List<String[]> list){
		SortedSet<String> set = getEtlJobs(list);
		Iterator it = set.iterator();
        List<JobEntity> jobEntities= new ArrayList<JobEntity>();
        while(it.hasNext()){
			String etlsysjob = it.next().toString();
			System.out.println("========etlsysjob=====" + etlsysjob);
			String etlsystem = etlsysjob.split("\\.")[0].toUpperCase();
			String etljob = etlsysjob.split("\\.")[1].toUpperCase();


			jobEntities = jobDao.selectList(new EntityWrapper<JobEntity>()
                        .eq("etl_System",etlsystem)
                        .eq("etl_Job",etljob)
                );


		}
		if(jobEntities.size()>0){
		    return true;
        }
		return false;
	}


    private Boolean checkJobDependencyEntityIsExist(List<String[]> list){
        SortedSet<String> set = getEtlJobs(list);
        Iterator it = set.iterator();
        List<JobDependencyEntity> entities= new ArrayList<JobDependencyEntity>();
        while(it.hasNext()){
            String etlsysjob = it.next().toString();
            String etlsystem = etlsysjob.split("\\.")[0].toUpperCase();
            String etljob = etlsysjob.split("\\.")[1].toUpperCase();
            entities = jobDependencyService.selectList(new EntityWrapper<JobDependencyEntity>()
                    .eq("etl_System",etlsystem)
                    .eq("etl_Job",etljob)
            );

        }
        if(entities.size()>0){
            return true;
        }
        return false;
    }

    private Boolean checkJobStreamEntityIsExist(List<String[]> list){
        HashSet<String> set = getEtlStreamJobs(list);
        Iterator it = set.iterator();
        List<JobStreamEntity> entities= new ArrayList<JobStreamEntity>();
        while(it.hasNext()){
            String etlsysjob = it.next().toString().split("\\-")[0].toUpperCase();
            String streamsysjob = it.next().toString().split("\\-")[1].toUpperCase();
            String etlsystem = etlsysjob.split("\\.")[0].toUpperCase();
            String etljob = etlsysjob.split("\\.")[1].toUpperCase();
            String streamSystem = streamsysjob.split("\\.")[0].toUpperCase();
            String streamjob = streamsysjob.split("\\.")[1].toUpperCase();
            entities = jobStreamService.selectList(new EntityWrapper<JobStreamEntity>()
                    .eq("etl_System",etlsystem)
                    .eq("etl_Job",etljob)
                    .eq("stream_System",streamSystem)
                    .eq("stream_Job",streamjob)
            );

        }
        if(entities.size()>0){
            return true;
        }
        return false;
    }

    private Boolean checkJobStepEntityIsExist(List<String[]> list){
        SortedSet<String> set = getEtlJobs(list);
        Iterator it = set.iterator();
        List<JobStepEntity> entities= new ArrayList<JobStepEntity>();
        while(it.hasNext()){
            String etlsysjob = it.next().toString();
            String etlsystem = etlsysjob.split("\\.")[0].toUpperCase();
            String etljob = etlsysjob.split("\\.")[1].toUpperCase();
            entities = jobStepService.selectList(new EntityWrapper<JobStepEntity>()
                    .eq("etl_System",etlsystem)
                    .eq("etl_Job",etljob)
            );

        }
        if(entities.size()>0){
            return true;
        }
        return false;
    }

    private Boolean checkJobSourceEntityIsExist(List<String[]> list){
        SortedSet<String> set = getEtlJobs(list);
        Iterator it = set.iterator();
        List<JobSourceEntity> entities= new ArrayList<JobSourceEntity>();
        while(it.hasNext()){
            String etlsysjob = it.next().toString();
            String etlsystem = etlsysjob.split("\\.")[0].toUpperCase();
            String etljob = etlsysjob.split("\\.")[1].toUpperCase();
            entities = jobSourceService.selectList(new EntityWrapper<JobSourceEntity>()
                    .eq("etl_System",etlsystem)
                    .eq("etl_Job",etljob)
            );

        }
        if(entities.size()>0){
            return true;
        }
        return false;
    }

    private Boolean checkJobTimewindowEntityIsExist(List<String[]> list){
        SortedSet<String> set = getEtlJobs(list);
        Iterator it = set.iterator();
        List<JobTimewindowEntity> entities= new ArrayList<JobTimewindowEntity>();
        while(it.hasNext()){
            String etlsysjob = it.next().toString();
            String etlsystem = etlsysjob.split("\\.")[0].toUpperCase();
            String etljob = etlsysjob.split("\\.")[1].toUpperCase();
            entities = jobTimewindowService.selectList(new EntityWrapper<JobTimewindowEntity>()
                    .eq("etl_System",etlsystem)
                    .eq("etl_Job",etljob)
            );

        }
        if(entities.size()>0){
            return true;
        }
        return false;
    }

	private SortedSet<String> getEtlJobs(List<String[]> list){
		SortedSet<String> sortedSet = new TreeSet<String>();
		for(String[] strings:list){
				String etlSysJob = strings[0].toUpperCase().trim()+"."+strings[1].toUpperCase().trim();
				sortedSet.add(etlSysJob);
		}
		return  sortedSet;
	}

    private HashSet<String> getEtlStreamJobs(List<String[]> list){
        HashSet<String> streamSet = new HashSet<String>();
        for(String[] strings:list){
            String triggerFlag = strings[4].toString();
            if(triggerFlag == "1") {
                String streamSysJob = strings[0].toUpperCase().trim()+"."+strings[1].toUpperCase().trim()+"-"+strings[2].toUpperCase().trim() + "." + strings[3].toUpperCase().trim();
                streamSet.add(streamSysJob);
            }
        }
        return  streamSet;
    }



	public List<ExlJobConfig> expJobConfig() {
//		List<JobEntity> jobEntities = jobDao.selectList(new EntityWrapper<JobEntity>().addFilter("enable='1'"));
		List<JobEntity> jobEntities = jobDao.selectList(new EntityWrapper<JobEntity>());
		List<ExlJobConfig> exlJobConfigs = new ArrayList<ExlJobConfig>();
		ExlJobConfig ejc=null;
		List<JobStepEntity> jobStepEntitys;
		List<JobDependencyEntity> jobDependencyEntities;
		Map map;
		Integer scriptid;
		JobStepEntity jse;
		ScriptEntity se;
		for (JobEntity je : jobEntities){
			ejc=new ExlJobConfig();
			ejc.setEtlSystem(je.getEtlSystem());
			ejc.setEtlJob(je.getEtlJob());
			ejc.setEtlServer(je.getEtlServer());
			ejc.setDescription(je.getDescription());
			ejc.setFrequency(je.getFrequency());
			ejc.setEnableFlag(je.getEnable());
			map = new HashMap();
			map.put("etl_System",je.getEtlSystem());
			map.put("etl_Job",je.getEtlJob());
//			map.put("enable",1);
			jobStepEntitys = jobStepService.selectByMap(map);
			jobDependencyEntities = jobDependencyService.selectByMap(map);

			scriptid = jobStepEntitys.get(0).getScriptid();
			ejc.setScriptID(scriptid.toString());
			try{
				se = scriptService.selectById(scriptid);
				ejc.setScriptFile(se.getFilename());
				ejc.setScriptType(se.getScripttype());
			}catch (Exception e){
				e.printStackTrace();
			}
			exlJobConfigs.add(ejc);
		}

		return exlJobConfigs;
	}


	public List<ExlJobDependency> expJobDependency() {
//		List<JobEntity> jobEntities = jobDao.selectList(new EntityWrapper<JobEntity>().addFilter("enable='1'"));
		List<JobEntity> jobEntities = jobDao.selectList(new EntityWrapper<JobEntity>());
		List<ExlJobDependency> exlJobDependencies = new ArrayList<ExlJobDependency>();
		List<JobDependencyEntity> jobDependencyEntities;
		ExlJobDependency ejd=null;
		Map map;
		Map map2;
		for (JobEntity je : jobEntities){
			map = new HashMap();
			map.put("etl_System",je.getEtlSystem());
			map.put("etl_Job",je.getEtlJob());
//			map.put("enable",1);
			jobDependencyEntities = jobDependencyService.selectByMap(map);
			for (JobDependencyEntity jde:jobDependencyEntities){
				ejd = new ExlJobDependency();
				ejd.setETLSystem(jde.getEtlSystem());
				ejd.setETLJob(jde.getEtlJob());
				ejd.setDependencySystem(jde.getDependencySystem());
				ejd.setDependencyJob(jde.getDependencyJob());
				ejd.setEnableFlag(jde.getEnable());
				map2 = new HashMap();
				map2.put("Stream_System",jde.getEtlSystem());
				map2.put("Stream_Job",jde.getEtlJob());
				map2.put("Etl_System",jde.getDependencySystem());
				map2.put("Etl_Job",jde.getDependencyJob());
				if(jobStreamService.selectByMap(map2).size()>0){
					ejd.setTriggerJobFlag("1");
				}else{
					ejd.setTriggerJobFlag("0");
				}
				exlJobDependencies.add(ejd);

			}

		}

		return exlJobDependencies;
	}



	public String expJobConfigFile(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String time = sdf.format(new Date());
		// 定义文件名
		String fileName = "job_batch_config_"+time+".xls";
        String filePath;
        String os = System.getProperty("os.name");
        filePath = systemParams.getConfigExportDir();

		// 定义表格标题
//		String headString1 = "ETL任务基础信息配置表";
		// 定义工作表表名
		String sheetName1 = "jobconfig";
		String sheetName2 = "jobdenpency";

		// 定义表头内容
		String[] thead1 = {"ETL_System\n(作业系统)","ETL_Job\n(作业名称)","ETL_Server\n(ETL服务器)","Description\n(作业描述)","Frequency\n(作业执行周期)","ScriptFile\n(作业模板脚本名称)","ScriptID\n(作业模板ID)","ScriptType\n(作业模板类型)","EnableFlag\n(作业是否有效)"};
		String[] thead2 = {"ETL_System\n(作业系统)","ETL_Job\n(作业名称)","Dependency_System\n(依赖作业系统)","Dependency_Job\n(依赖作业名称)","Trigger_Job_Flag触发作业标志","EnableFlag\n(依赖是否有效)"};

		// 定义每一列宽度
		int[] sheetWidth1 = {5000,5000,5000,8000,5000,5000,5000,5000,5000};
		int[] sheetWidth2 = {5000,8000,6000,8000,5000,5000};

		List<ExlJobConfig>  exlJobConfigs = expJobConfig();
		List<Map<String, String>> rs1 = ExcelTool.createJobConfigDataSet(exlJobConfigs);

		List<ExlJobDependency>  exlJobDependencies = expJobDependency();
		List<Map<String, String>> rs2 = ExcelTool.createJobDependencyDataSet(exlJobDependencies);



		// 创建Excel文档对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建工作表
		HSSFSheet sheet1 = wb.createSheet(sheetName1);
		HSSFSheet sheet2 = wb.createSheet(sheetName2);


		ExcelTool.createThead(wb,sheet1,thead1,sheetWidth1);
		ExcelTool.createTable(wb,sheet1,rs1);
		ExcelTool.createThead(wb,sheet2,thead2,sheetWidth2);
		ExcelTool.createTable(wb,sheet2,rs2);
		FileOutputStream fos = null;
		try {
			File dir = new File(filePath);
			if(!dir.exists())
				dir.mkdirs();
			fos = new FileOutputStream(new File(filePath+fileName));
			wb.write(fos);
			fos.close();
			wb.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}

		return fileName;
	}
}
