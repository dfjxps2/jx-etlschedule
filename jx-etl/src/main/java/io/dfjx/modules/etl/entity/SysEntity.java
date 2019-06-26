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
 * @date 2018-07-23 22:44:03
 */
@TableName("etl_sys")
public class SysEntity implements Serializable {
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
	private String description;
	/**
	 * 
	 */
	private Integer datakeepperiod;
	/**
	 * 
	 */
	private Integer logkeepperiod;
	/**
	 * 
	 */
	private Integer recordkeepperiod;
	/**
	 * 
	 */
	private Integer priority;
	/**
	 * 
	 */
	private Integer concurrent;

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
	public void setDatakeepperiod(Integer datakeepperiod) {
		this.datakeepperiod = datakeepperiod;
	}
	/**
	 * 获取：
	 */
	public Integer getDatakeepperiod() {
		return datakeepperiod;
	}
	/**
	 * 设置：
	 */
	public void setLogkeepperiod(Integer logkeepperiod) {
		this.logkeepperiod = logkeepperiod;
	}
	/**
	 * 获取：
	 */
	public Integer getLogkeepperiod() {
		return logkeepperiod;
	}
	/**
	 * 设置：
	 */
	public void setRecordkeepperiod(Integer recordkeepperiod) {
		this.recordkeepperiod = recordkeepperiod;
	}
	/**
	 * 获取：
	 */
	public Integer getRecordkeepperiod() {
		return recordkeepperiod;
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
	/**
	 * 设置：
	 */
	public void setConcurrent(Integer concurrent) {
		this.concurrent = concurrent;
	}
	/**
	 * 获取：
	 */
	public Integer getConcurrent() {
		return concurrent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
