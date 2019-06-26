package io.dfjx.modules.etl.controller;

import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.dfjx.modules.etl.service.ScriptLogService;
import io.dfjx.modules.etl.util.ChmodUtil;
import io.dfjx.modules.etl.util.ReadFileUtil;
import io.dfjx.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.dfjx.common.config.SystemParams;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;
import io.dfjx.common.validator.ValidatorUtils;
import io.dfjx.modules.etl.entity.ScriptEntity;
import io.dfjx.modules.etl.service.ScriptService;
import org.springframework.util.FileCopyUtils;


/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 14:38:56
 */
@RestController
@RequestMapping("etl/script")
public class ScriptController {
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
		String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
		params.put("username",username);
		params.put("shareflag","1");//只显示本人所属和共享脚本
		PageUtils page = scriptService.queryPage(params);

		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{scriptid}")
	@RequiresPermissions("etl:script:info")
	public R info(@PathVariable("scriptid") Integer scriptid){
		ScriptEntity script = scriptService.selectById(scriptid);

		return R.ok().put("script", script);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("etl:script:save")
	public R save(@RequestBody ScriptEntity script){
		String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
		script.setUsername(username);
		scriptService.insert(script);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("etl:script:update")
	public R update(@RequestBody ScriptEntity script){
		ValidatorUtils.validateEntity(script);
		scriptService.updateAllColumnById(script);//全部更新

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("etl:script:delete")
	public R delete(@RequestBody Integer[] scriptids){
		scriptService.deleteBatchIds(Arrays.asList(scriptids));

		return R.ok();
	}

	/**
	 * 上传
	 * 
	 * @param
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "upload")
	@RequiresPermissions("etl:script:upload")
	@ResponseBody
	public R upload(@RequestParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse resp) {
		//String contentType = file.getContentType();

		String uppercase=request.getParameter("uppercase");
		String filePath= systemParams.getPublicScriptUploadDir();
		String fileName = file.getOriginalFilename();
		if(StringUtils.isNotBlank(uppercase)){
			fileName=fileName.toUpperCase();
		}
		filePath=filePath+fileName;
		try {
			File targetFile = new File(filePath);  
			if(targetFile.exists()){    
				return R.error(1, "脚本已存在");
				//targetFile.delete();    
			}  
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(file.getBytes());
			out.flush();
			out.close();
			ChmodUtil.addExt(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok();
	}
	@RequestMapping("/getScriptPath") 
	@RequiresPermissions("etl:script:getScriptPath")
	public R getScriptPath(){
		String filePath= systemParams.getPublicScriptUploadDir();
		return R.ok().put("filePath",filePath);
	}


	@RequestMapping("/getScripts") 
	@RequiresPermissions("etl:script:getScripts")
	public R getService(){
		List<Map<String, Object>> listMap  = scriptService.getScripts();
		return R.ok().put("doScripts",listMap);
	}

	@RequestMapping("/readScripts/{scriptid}")
	//@RequiresPermissions(value={"etl:script:readScripts","etl:script:editScripts"},logical= Logical.OR)
	public R readScripts(@PathVariable("scriptid") Integer scriptid){
		ScriptEntity script = scriptService.selectById(scriptid);
		String filePath= systemParams.getPublicScriptUploadDir();
		String fileName = script.getFilename();

		filePath=filePath+fileName;
		System.out.println("readScript-filePath==============" + filePath);
		String logresult = ReadFileUtil.readToString(filePath);
		if(logresult == "-1"){
			return R.error("该脚本不存在");
		}
		return R.ok().put("scriptDetail",logresult).put("type", script.getScripttype());
	}

	@RequestMapping("/saveScripts")
	//@RequiresPermissions("etl:script:editScripts")
	public R saveScripts(@RequestBody Map<String, Object> params){
		String scriptDetail = putVal(params,"scriptDetail");
		String scriptid = putVal(params,"scriptid");
		if(scriptDetail == null || scriptDetail.length() == 0){
			return R.error("脚本内容不能为空");
		}
		scriptDetail = base64Decode(scriptDetail);
		ScriptEntity script = scriptService.selectById(scriptid);
		String uploadDir= systemParams.getPublicScriptUploadDir();
		String fileName = script.getFilename();
		String filePath=uploadDir+fileName;
		Date logDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String version = dateFormat.format(logDate);

		try {
			File targetFile = new File(filePath);

			//1.备份旧脚本
			String[] arr = fileName.split("\\.");
			String backupPath = uploadDir + "BACKUP" + File.separator;
			File backupDir = new File(backupPath);
			if(!backupDir.exists())
				backupDir.mkdir();
			String backupName = arr[0] + "_" + version + "." + arr[1];
			backupPath = backupPath + backupName;
			File backupFile = new File(backupPath);
			FileCopyUtils.copy(targetFile, backupFile);

			FileOutputStream out = new FileOutputStream(filePath);
			out.write(scriptDetail.getBytes());
			out.flush();
			out.close();
			ChmodUtil.addExt(targetFile);

			//添加版本信息
			String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
			scriptLogService.addLog(script, version, "更新", "修改脚本内容", username, logDate, backupName);

		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}
	private String putVal(Map<String, Object> map, String key){
		if(!map.containsKey(key))
			return "";
		return map.get(key).toString();
	}
	private String base64Decode(String str){

		Base64.Decoder decoder = Base64.getDecoder();
		try {
			return URLDecoder.decode(new String(decoder.decode(str), "UTF-8"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
