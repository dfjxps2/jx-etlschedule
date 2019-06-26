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
 * @date 2018-08-07 15:37:39
 */
@TableName("etl_job_source")
public class JobSourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String source;
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
	private String convFileHead;
	/**
	 * 
	 */
	private String autofilter;
	/**
	 * 
	 */
	private String alert;
	/**
	 * 
	 */
	private Integer beforehour;
	/**
	 * 
	 */
	private Integer beforemin;
	/**
	 * 
	 */
	private Integer offsetday;
	/**
	 * 
	 */
	private Integer lastcount;

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
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * 获取：
	 */
	public String getSource() {
		return source;
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
	public void setConvFileHead(String convFileHead) {
		this.convFileHead = convFileHead;
	}
	/**
	 * 获取：
	 */
	public String getConvFileHead() {
		return convFileHead;
	}
	/**
	 * 设置：
	 */
	public void setAutofilter(String autofilter) {
		this.autofilter = autofilter;
	}
	/**
	 * 获取：
	 */
	public String getAutofilter() {
		return autofilter;
	}
	/**
	 * 设置：
	 */
	public void setAlert(String alert) {
		this.alert = alert;
	}
	/**
	 * 获取：
	 */
	public String getAlert() {
		return alert;
	}
	/**
	 * 设置：
	 */
	public void setBeforehour(Integer beforehour) {
		this.beforehour = beforehour;
	}
	/**
	 * 获取：
	 */
	public Integer getBeforehour() {
		return beforehour;
	}
	/**
	 * 设置：
	 */
	public void setBeforemin(Integer beforemin) {
		this.beforemin = beforemin;
	}
	/**
	 * 获取：
	 */
	public Integer getBeforemin() {
		return beforemin;
	}
	/**
	 * 设置：
	 */
	public void setOffsetday(Integer offsetday) {
		this.offsetday = offsetday;
	}
	/**
	 * 获取：
	 */
	public Integer getOffsetday() {
		return offsetday;
	}
	/**
	 * 设置：
	 */
	public void setLastcount(Integer lastcount) {
		this.lastcount = lastcount;
	}
	/**
	 * 获取：
	 */
	public Integer getLastcount() {
		return lastcount;
	}
}
