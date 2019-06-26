package io.dfjx.modules.etl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-25 15:22:45
 */
@TableName("etl_job_log")
public class JobLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;

	private String etlSystem;
	/**
	 * 
	 */
	private String etlJob;
	/**
	 * 
	 */
	private Integer jobsessionid;
	/**
	 * 
	 */
	private String jobstepid;
	/**
	 * 
	 */
	private String scriptfile;
	/**
	 * 
	 */
	private Date txdate;
	/**
	 * 
	 */
	private String starttime;
	/**
	 * 
	 */
	private String endtime;
	/**
	 * 
	 */
	private Integer returncode;
	/**
	 * 
	 */
	private Integer seconds;

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
	public void setJobstepid(String jobstepid) {
		this.jobstepid = jobstepid;
	}
	/**
	 * 获取：
	 */
	public String getJobstepid() {
		return jobstepid;
	}
	/**
	 * 设置：
	 */
	public void setScriptfile(String scriptfile) {
		this.scriptfile = scriptfile;
	}
	/**
	 * 获取：
	 */
	public String getScriptfile() {
		return scriptfile;
	}
	/**
	 * 设置：
	 */
	public void setTxdate(Date txdate) {
		this.txdate = txdate;
	}
	/**
	 * 获取：
	 */
	public Date getTxdate() {
		return txdate;
	}
	/**
	 * 设置：
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	/**
	 * 获取：
	 */
	public String getStarttime() {
		return starttime;
	}
	/**
	 * 设置：
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	/**
	 * 获取：
	 */
	public String getEndtime() {
		return endtime;
	}
	/**
	 * 设置：
	 */
	public void setReturncode(Integer returncode) {
		this.returncode = returncode;
	}
	/**
	 * 获取：
	 */
	public Integer getReturncode() {
		return returncode;
	}
	/**
	 * 设置：
	 */
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}
	/**
	 * 获取：
	 */
	public Integer getSeconds() {
		return seconds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
