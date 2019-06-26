package io.dfjx.modules.etl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-24 15:17:47
 */
@TableName("etl_job")
public class JobEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;
	/**
	 * 
	 */

	private String etlSystem;
	/**
	 * 
	 */
	private String etlJob;
	/**
	 * 
	 */
	private String etlServer;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private String frequency;
	/**
	 * 
	 */
	private String jobtype;
	/**
	 * 
	 */
	private String enable;
	/**
	 * 
	 */
	private String lastStarttime;
	/**
	 * 
	 */
	private String lastEndtime;
	/**
	 * 
	 */
	private String lastJobstatus;
	/**
	 * 
	 */
	private Date lastTxdate;
	/**
	 * 
	 */
	private Integer lastFilecnt;
	/**
	 * 
	 */
	private String lastCubestatus;
	/**
	 * 
	 */
	private String cubeflag;
	/**
	 * 
	 */
	private String checkflag;
	/**
	 * 
	 */
	private String autooff;
	/**
	 * 
	 */
	private String checkcalendar;
	/**
	 * 
	 */
	private String calendarbu;
	/**
	 * 
	 */
	private String runningscript;
	/**
	 * 
	 */
	private Integer jobsessionid;
	/**
	 * 
	 */
	private Integer expectedrecord;
	/**
	 * 
	 */
	private String checklaststatus;
	/**
	 * 
	 */
	private String timetrigger;
	/**
	 * 
	 */
	private Integer priority;
	/**
	 * 公共脚本
	 */
	@TableField(exist=false)
	private Integer publicScript;
	
	/**
	 * 是否为触发作业
	 */
	@TableField(exist=false)
	private String isTriggerJob;
	/**
	 * 所有触发作业列表
	 */
	@TableField(exist=false)
	private String[] allDependSave;

	/**
	 * 触发作业id
	 */
	@TableField(exist=false)
	private String triggerJob;
	
	
	/**
	 * 设置：
	 */
	public void setEtlSystem(String etlSystem) {
		this.etlSystem = etlSystem;
	}
	/**
	 * 获取：
	 */
	public String getEtlSystem() {
		return etlSystem;
	}
	/**
	 * 设置：
	 */
	public void setEtlJob(String etlJob) {
		this.etlJob = etlJob;
	}
	/**
	 * 获取：
	 */
	public String getEtlJob() {
		return etlJob;
	}
	/**
	 * 设置：
	 */
	public void setEtlServer(String etlServer) {
		this.etlServer = etlServer;
	}
	/**
	 * 获取：
	 */
	public String getEtlServer() {
		return etlServer;
	}
	/**
	 * 设置：
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	/**
	 * 获取：
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * 设置：
	 */
	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}
	/**
	 * 获取：
	 */
	public String getJobtype() {
		return jobtype;
	}
	/**
	 * 设置：
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}
	/**
	 * 获取：
	 */
	public String getEnable() {
		return enable;
	}
	/**
	 * 设置：
	 */
	public void setLastStarttime(String lastStarttime) {
		this.lastStarttime = lastStarttime;
	}
	/**
	 * 获取：
	 */
	public String getLastStarttime() {
		return lastStarttime;
	}
	/**
	 * 设置：
	 */
	public void setLastEndtime(String lastEndtime) {
		this.lastEndtime = lastEndtime;
	}
	/**
	 * 获取：
	 */
	public String getLastEndtime() {
		return lastEndtime;
	}
	/**
	 * 设置：
	 */
	public void setLastJobstatus(String lastJobstatus) {
		this.lastJobstatus = lastJobstatus;
	}
	/**
	 * 获取：
	 */
	public String getLastJobstatus() {
		return lastJobstatus;
	}
	/**
	 * 设置：
	 */
	public void setLastTxdate(Date lastTxdate) {
		this.lastTxdate = lastTxdate;
	}
	/**
	 * 获取：
	 */
	public Date getLastTxdate() {
		return lastTxdate;
	}
	/**
	 * 设置：
	 */
	public void setLastFilecnt(Integer lastFilecnt) {
		this.lastFilecnt = lastFilecnt;
	}
	/**
	 * 获取：
	 */
	public Integer getLastFilecnt() {
		return lastFilecnt;
	}
	/**
	 * 设置：
	 */
	public void setLastCubestatus(String lastCubestatus) {
		this.lastCubestatus = lastCubestatus;
	}
	/**
	 * 获取：
	 */
	public String getLastCubestatus() {
		return lastCubestatus;
	}
	/**
	 * 设置：
	 */
	public void setCubeflag(String cubeflag) {
		this.cubeflag = cubeflag;
	}
	/**
	 * 获取：
	 */
	public String getCubeflag() {
		return cubeflag;
	}
	/**
	 * 设置：
	 */
	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}
	/**
	 * 获取：
	 */
	public String getCheckflag() {
		return checkflag;
	}
	/**
	 * 设置：
	 */
	public void setAutooff(String autooff) {
		this.autooff = autooff;
	}
	/**
	 * 获取：
	 */
	public String getAutooff() {
		return autooff;
	}
	/**
	 * 设置：
	 */
	public void setCheckcalendar(String checkcalendar) {
		this.checkcalendar = checkcalendar;
	}
	/**
	 * 获取：
	 */
	public String getCheckcalendar() {
		return checkcalendar;
	}
	/**
	 * 设置：
	 */
	public void setCalendarbu(String calendarbu) {
		this.calendarbu = calendarbu;
	}
	/**
	 * 获取：
	 */
	public String getCalendarbu() {
		return calendarbu;
	}
	/**
	 * 设置：
	 */
	public void setRunningscript(String runningscript) {
		this.runningscript = runningscript;
	}
	/**
	 * 获取：
	 */
	public String getRunningscript() {
		return runningscript;
	}
	/**
	 * 设置：
	 */
	public void setJobsessionid(Integer jobsessionid) {
		this.jobsessionid = jobsessionid;
	}
	/**
	 * 获取：
	 */
	public Integer getJobsessionid() {
		return jobsessionid;
	}
	/**
	 * 设置：
	 */
	public void setExpectedrecord(Integer expectedrecord) {
		this.expectedrecord = expectedrecord;
	}
	/**
	 * 获取：
	 */
	public Integer getExpectedrecord() {
		return expectedrecord;
	}
	/**
	 * 设置：
	 */
	public void setChecklaststatus(String checklaststatus) {
		this.checklaststatus = checklaststatus;
	}
	/**
	 * 获取：
	 */
	public String getChecklaststatus() {
		return checklaststatus;
	}
	/**
	 * 设置：
	 */
	public void setTimetrigger(String timetrigger) {
		this.timetrigger = timetrigger;
	}
	/**
	 * 获取：
	 */
	public String getTimetrigger() {
		return timetrigger;
	}
	/**
	 * 设置：
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	/**
	 * 获取：
	 */
	public Integer getPriority() {
		return priority;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPublicScript() {
		return publicScript;
	}
	public void setPublicScript(Integer publicScript) {
		this.publicScript = publicScript;
	}
	public String getIsTriggerJob() {
		return isTriggerJob;
	}
	public void setIsTriggerJob(String isTriggerJob) {
		this.isTriggerJob = isTriggerJob;
	}
	public String[] getAllDependSave() {
		return allDependSave;
	}
	public void setAllDependSave(String[] allDependSave) {
		this.allDependSave = allDependSave;
	}
	public String getTriggerJob() {
		return triggerJob;
	}
	public void setTriggerJob(String triggerJob) {
		this.triggerJob = triggerJob;
	}

	
}
