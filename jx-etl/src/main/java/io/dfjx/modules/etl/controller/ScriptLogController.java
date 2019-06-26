package io.dfjx.modules.etl.controller;

import io.dfjx.common.config.SystemParams;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;
import io.dfjx.modules.etl.entity.ScriptEntity;
import io.dfjx.modules.etl.entity.ScriptLogEntity;
import io.dfjx.modules.etl.service.ScriptLogService;
import io.dfjx.modules.etl.service.ScriptService;
import io.dfjx.modules.etl.util.ChmodUtil;
import io.dfjx.modules.etl.util.ReadFileUtil;
import io.dfjx.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by cc on 2018/10/8.
 */
@RestController
@RequestMapping("etl/scriptlog")
public class ScriptLogController {
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private ScriptLogService scriptLogService;
    @Autowired
    private SystemParams systemParams;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:script:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = scriptLogService.queryPage(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("/readScripts/{id}")
    @RequiresPermissions("etl:script:list")
    public R readScripts(@PathVariable("id") Integer id){
        ScriptLogEntity script = scriptLogService.selectById(id);
        String filePath= systemParams.getPublicScriptUploadDir() + "BACKUP" + File.separator;
        String fileName = script.getVersionfile();
        filePath=filePath+fileName;
        String logresult = ReadFileUtil.readToString(filePath);
        if(logresult == "-1"){
            return R.error("该脚本不存在");
        }
        return R.ok().put("scriptDetail",logresult).put("type", script.getScripttype());
    }

    @RequestMapping("/saveScripts/{id}")
    @RequiresPermissions("etl:script:list")
    public R saveScripts(@PathVariable("id") Integer id){
        ScriptLogEntity scriptLog = scriptLogService.selectById(id);
        ScriptEntity script = scriptService.selectById(scriptLog.getScriptid());

        Date logDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String version = dateFormat.format(logDate);

        String uploadDirPath= systemParams.getPublicScriptUploadDir();
        String backupDirPath=uploadDirPath + "BACKUP" + File.separator;
        String fileName = script.getFilename();
        String filePath=uploadDirPath+fileName;

        String[] arr = fileName.split("\\.");
        String backupName = arr[0] + "_" + version + "." + arr[1];
        String backupFilePath=backupDirPath + backupName;

        String restorePath = backupDirPath + scriptLog.getVersionfile();


        try {
            File targetFile = new File(filePath);
            //1.将当前脚本备份，用指定版本覆盖当前脚本
            File backupFile = new File(backupFilePath);
            FileCopyUtils.copy(targetFile, backupFile);

            File restoreFile = new File(restorePath);
            FileCopyUtils.copy(restoreFile, targetFile);

            //添加版本信息
            String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
            scriptLogService.addLog(script, version, "还原", "恢复脚本内容", username, logDate, backupName);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }
}
