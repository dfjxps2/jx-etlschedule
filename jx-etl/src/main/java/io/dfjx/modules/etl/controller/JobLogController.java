package io.dfjx.modules.etl.controller;

import io.dfjx.common.config.SystemParams;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;
import io.dfjx.common.validator.ValidatorUtils;
import io.dfjx.modules.etl.entity.JobLogEntity;
import io.dfjx.modules.etl.service.JobLogService;
import io.dfjx.modules.etl.util.DownLoadFileUtil;
import io.dfjx.modules.etl.util.ReadFileUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 *
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-25 15:22:45
 */
@RestController
@RequestMapping("etl/joblog")
public class JobLogController {
    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private SystemParams systemParams;
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("etl:joblog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{etlSystem}")
//    @RequiresPermissions("etl:joblog:info")
    public R info(@PathVariable("etlSystem") String etlSystem){
        JobLogEntity jobLog = jobLogService.selectById(etlSystem);

        return R.ok().put("jobLog", jobLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("etl:joblog:save")
    public R save(@RequestBody JobLogEntity jobLog){
        jobLogService.insert(jobLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("etl:joblog:update")
    public R update(@RequestBody JobLogEntity jobLog){
        ValidatorUtils.validateEntity(jobLog);
        jobLogService.updateAllColumnById(jobLog);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("etl:joblog:delete")
    public R delete(@RequestBody String[] etlSystems){
        jobLogService.deleteBatchIds(Arrays.asList(etlSystems));

        return R.ok();
    }


    /**
     * 查看作业日志详情
     */
    @RequestMapping("/loadlog")
//    @RequiresPermissions("etl:joblog:loadlog")
    public R loadlog(@RequestParam Map<String, Object> params){
        logger.info("loadlog-param==============" + params.toString());
        String etlSystem = params.get("etlSystem").toString();
        Integer jobsessionid = Integer.parseInt(params.get("jobsessionid").toString());
        String etlJob = params.get("etlJob").toString();
        String txdate = params.get("txdate").toString();

        String txdatanew = jobLogService.getLogDir(etlSystem,jobsessionid,etlJob,txdate);
        String filename = etlJob + "." + jobsessionid + ".log";
        String filepathname = systemParams.getFileDownloadDir() + etlSystem + "/" + txdatanew + "/" + filename.toLowerCase();
        logger.info("日志名称  "+ filepathname);
        String logresult = ReadFileUtil.readToString(filepathname);
        if(logresult == "-1"){
            return R.error("该日志不存在或已被清理");
        }

        return R.ok().put("logresult",logresult);
    }



    /**
     * 下载选中日志
     */
    @RequestMapping("/logdload")
//    @RequiresPermissions("etl:joblog:logdload")
    public void logdload(HttpServletRequest request, HttpServletResponse response) {
        String etlSystem = request.getParameter("etlSystem");
        Integer jobsessionid = Integer.parseInt(request.getParameter("jobsessionid"));
        String etlJob = request.getParameter("etlJob");
        String txdate = request.getParameter("txdate");

        String txdatanew = jobLogService.getLogDir(etlSystem,jobsessionid,etlJob,txdate);
        String filename = etlJob + "." + jobsessionid + ".log";
        String filepath= systemParams.getFileDownloadDir() + etlSystem + "/" + txdatanew + "/";
        File file = new File(filepath+filename.toLowerCase());
        if (!file.exists()){
              logger.info("日志文件不存在或已被清理");
        }

        DownLoadFileUtil.downloadFile(response,filepath,filename.toLowerCase());
    }

    /**
     * 查看最新作业日志详情
     */
    @RequestMapping("/loadLast")
//    @RequiresPermissions("etl:joblog:loadlog")
    public R loadLast(@RequestParam Map<String, Object> params){
        String etlSystem = params.get("etlSystem").toString();
        String etlJob = params.get("etlJob").toString();

        String filepathname = jobLogService.getLastLogPath(systemParams.getFileDownloadDir(), etlSystem,etlJob);
        if (filepathname==null || "".equals(filepathname)){
            return R.error("日志文件不存在或已被清理");
        }
        logger.info("日志名称  "+ filepathname);
        File file = new File(filepathname);
        if (!file.exists()){
            return R.error("日志文件不存在或已被清理");
        }
        String logresult = ReadFileUtil.readToString(filepathname);
        if(logresult == "-1"){
            return R.error("该日志不存在或已被清理");
        }

        return R.ok().put("logresult",logresult);
    }

    /**
     * 下载最新日志
     */
    @RequestMapping("/downloadLast")
//    @RequiresPermissions("etl:joblog:logdload")
    public void downloadLast(HttpServletRequest request, HttpServletResponse response) {
        String etlSystem = request.getParameter("etlSystem");
        String etlJob = request.getParameter("etlJob");

        String filepath= jobLogService.getLastLogPath(systemParams.getFileDownloadDir(), etlSystem, etlJob);
        if (filepath==null || "".equals(filepath)){
            logger.info("日志文件不存在或已被清理");
        }
        int start = filepath.lastIndexOf("/");
        String filename = filepath.substring(start + 1);
        filepath = filepath.substring(0, filepath.length() - filename.length());
        File file = new File(filepath);
        if (!file.exists()){
            logger.info("日志文件不存在或已被清理");
        }

        DownLoadFileUtil.downloadFile(response,filepath,filename);
    }


    @GetMapping("/triggerChartDate")
    public R triggerChartDate(@RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate) {
        Date now = null;
        Date preDate = null;
        SimpleDateFormat dateFormat = null;
        Date fromDate = null;

        if (startDate == null || endDate == null) {
            now = new Date();
            preDate = DateUtils.addDays(now, -1);
            fromDate = DateUtils.addDays(now, -6);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            startDate = dateFormat.format(fromDate);
            endDate = dateFormat.format(preDate);
        }

        Map lineChartData = jobLogService.queryLineChartData(startDate, endDate);
        Map pieChartData = jobLogService.queryPieChartData(startDate, endDate);

        return R.ok().put("lineChartData", lineChartData).put("pieChartData", pieChartData);
    }

    public static void main(String[] args) {
        JobLogController jobLogController = new JobLogController();
        Map<String,Object> map = new HashedMap();
        map.put("etlSystem","SDT");
        map.put("jobsessionid","2");
        map.put("scriptfile","test10100.sh");
        map.put("txdate","2018-07-24");
    }
}
