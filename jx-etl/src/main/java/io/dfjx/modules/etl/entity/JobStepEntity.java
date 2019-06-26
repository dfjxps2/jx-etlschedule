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
 * @date 2018-08-07 15:49:14
 */
@TableName("etl_job_step")
public class JobStepEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
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
	private String jobstepid;
	/**
	 * 
	 */
	private Integer scriptid;
	/**
	 * 
	 */
	private String scriptfile;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private String enable;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
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
	public void setScriptid(Integer scriptid) {
		this.scriptid = scriptid;
	}
	/**
	 * 获取：
	 */
	public Integer getScriptid() {
		return scriptid;
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
	public void setEnable(String enable) {
		this.enable = enable;
	}
	/**
	 * 获取：
	 */
	public String getEnable() {
		return enable;
	}
}
