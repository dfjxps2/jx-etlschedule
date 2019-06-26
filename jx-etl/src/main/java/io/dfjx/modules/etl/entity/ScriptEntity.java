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
@TableName("etl_script")
public class ScriptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer scriptid;
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
	private String username;
	/**
	 *
	 */
	private String shareflag;
	/**
	 *
	 */
	private String enable;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getShareflag() {
		return shareflag;
	}

	public void setShareflag(String shareflag) {
		this.shareflag = shareflag;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
