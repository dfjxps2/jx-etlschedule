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
 * @date 2018-07-24 14:38:56
 */
@TableName("etl_script_log")
public class ScriptLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Integer scriptid;
	/**
	 *
	 */
	private String scriptversion;
	/**
	 * 
	 */
	private String etlServer;
	/**
	 * 服务器名称
	 */
	@TableField(exist=false)
	private String etlServerName;
	/**
	 * 
	 */
	private String filepath;
	/**
	 * 
	 */
	private String filename;
	/**
	 * 
	 */
	private String scripttype;
	/**
	 * 
	 */
	private String description;
	/**
	 *
	 */
	private String actions;
	/**
	 *
	 */
	private String message;
	/**
	 *
	 */
	private String author;
	/**
	 *
	 */
	private Date logdate;
	/**
	 *
	 */
	private String versionfile;
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
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	/**
	 * 获取：
	 */
	public String getFilepath() {
		return filepath;
	}
	/**
	 * 设置：
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * 获取：
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * 设置：
	 */
	public void setScripttype(String scripttype) {
		this.scripttype = scripttype;
	}
	/**
	 * 获取：
	 */
	public String getScripttype() {
		return scripttype;
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
	public String getEtlServerName() {
		return etlServerName;
	}
	public void setEtlServerName(String etlServerName) {
		this.etlServerName = etlServerName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScriptversion() {
		return scriptversion;
	}

	public void setScriptversion(String scriptversion) {
		this.scriptversion = scriptversion;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getLogdate() {
		return logdate;
	}

	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}

	public String getVersionfile() {
		return versionfile;
	}

	public void setVersionfile(String versionfile) {
		this.versionfile = versionfile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
