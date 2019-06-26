package io.dfjx.modules.etl.service.impl;

import io.dfjx.modules.etl.entity.JobEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobLogDao;
import io.dfjx.modules.etl.entity.JobLogEntity;
import io.dfjx.modules.etl.service.JobLogService;


@Service("jobLogService")
public class JobLogServiceImpl extends ServiceImpl<JobLogDao, JobLogEntity> implements JobLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        System.out.println("===================" + params.toString());
        Page<JobLogEntity> page = new Page<JobLogEntity>();
        if(!params.containsKey("etlJob")){
            page = this.selectPage(
                    new Query<JobLogEntity>(params).getPage(),
                    new EntityWrapper<JobLogEntity>()
            );
            return new PageUtils(page);
        } else {
            String etlJob = params.get("etlJob").toString().toLowerCase();
            String etlSystem = params.get("etlSystem").toString();
            String txdate = params.get("txdate").toString();
            page = this.selectPage(
                    new Query<JobLogEntity>(params).getPage(),
                    new EntityWrapper<JobLogEntity>()
                            .like(StringUtils.isNotBlank(etlJob), "etl_Job", etlJob)
                            .like(StringUtils.isNotBlank(etlSystem), "etl_system", etlSystem)
                            .like(StringUtils.isNotBlank(txdate), "txdate", txdate)
                            .orderBy("txdate", false)
                            .orderBy("jobsessionid", false)
            );

        }
        return new PageUtils(page);
    }

    public String getLogDir(String etlSystem,Integer jobsessionid,String scriptfile,String txdate ){

        JobLogEntity jobLogEntity = this.selectOne(new EntityWrapper<JobLogEntity>()
                            .eq("etl_system",etlSystem)
                            .eq("jobsessionid",jobsessionid)
                            .eq("scriptfile",scriptfile)
                            .eq("txdate",txdate)

        );
        if(jobLogEntity == null){
            return null;
        }else{
            String[] datesplit =  jobLogEntity.getStarttime().substring(0,10).split("-");
            return datesplit[0]+datesplit[1]+datesplit[2];
        }

    }

    @Override
    public String getLastLogPath(String baseDir,String etlSystem, String etlJob) {
        JobLogEntity jobLogEntity = this.selectOne(new EntityWrapper<JobLogEntity>()
                .eq("etl_system",etlSystem)
                .eq("etl_job",etlJob)
                .orderBy("txdate", false)
                .orderBy("jobsessionid", false)
        );
        if(jobLogEntity == null){
            return null;
        }else{
            String[] datesplit =  jobLogEntity.getStarttime().substring(0,10).split("-");
            String datedir = datesplit[0]+datesplit[1]+datesplit[2];
            String filename = jobLogEntity.getScriptfile() + "." + jobLogEntity.getJobsessionid() + ".log";
            String pathname = baseDir + etlSystem + "/" + datedir + "/" + filename;
            return pathname;
        }
    }


}
