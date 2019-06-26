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
 * @date 2018-08-07 10:42:51
 */
@TableName("etl_job_stream")
public class JobStreamEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private String streamSystem;
	/**
	 * 
	 */
	private String streamJob;
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
	public void setStreamSystem(String streamSystem) {
		this.streamSystem = streamSystem;
	}
	/**
	 * 获取：
	 */
	public String getStreamSystem() {
		return streamSystem;
	}
	/**
	 * 设置：
	 */
	public void setStreamJob(String streamJob) {
		this.streamJob = streamJob;
	}
	/**
	 * 获取：
	 */
	public String getStreamJob() {
		return streamJob;
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
