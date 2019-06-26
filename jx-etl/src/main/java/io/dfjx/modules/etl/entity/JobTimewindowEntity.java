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
 * @date 2018-08-09 18:05:44
 */
@TableName("etl_job_timewindow")
public class JobTimewindowEntity implements Serializable {
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
	private String allow;
	/**
	 * 
	 */
	private Integer beginhour;
	/**
	 * 
	 */
	private Integer endhour;

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
	public void setAllow(String allow) {
		this.allow = allow;
	}
	/**
	 * 获取：
	 */
	public String getAllow() {
		return allow;
	}
	/**
	 * 设置：
	 */
	public void setBeginhour(Integer beginhour) {
		this.beginhour = beginhour;
	}
	/**
	 * 获取：
	 */
	public Integer getBeginhour() {
		return beginhour;
	}
	/**
	 * 设置：
	 */
	public void setEndhour(Integer endhour) {
		this.endhour = endhour;
	}
	/**
	 * 获取：
	 */
	public Integer getEndhour() {
		return endhour;
	}
}
