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
 * @date 2018-07-23 21:54:15
 */
@TableName("etl_server")
public class ServerEntity implements Serializable {
	private static final long serialVersionUID = 1L;



	@TableId
	private String id;
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
	private String ipaddress;
	/**
	 * 
	 */
	private Integer agentport;
	/**
	 * 
	 */
	private Integer livecount;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	/**
	 * 获取：
	 */
	public String getIpaddress() {
		return ipaddress;
	}
	/**
	 * 设置：
	 */
	public void setAgentport(Integer agentport) {
		this.agentport = agentport;
	}
	/**
	 * 获取：
	 */
	public Integer getAgentport() {
		return agentport;
	}
	/**
	 * 设置：
	 */
	public void setLivecount(Integer livecount) {
		this.livecount = livecount;
	}
	/**
	 * 获取：
	 */
	public Integer getLivecount() {
		return livecount;
	}
}
