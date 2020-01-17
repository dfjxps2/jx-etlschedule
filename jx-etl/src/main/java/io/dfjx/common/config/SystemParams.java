package io.dfjx.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system-params")
public class SystemParams {

	private String fileDownloadDir;
	private String publicScriptUploadDir;
	private String hqlScriptUploadDir;
	private String controlFileDir;
	private String execFileLnDir;
	private String createLnFlag;
	private String configExportDir;
	private String casServiceUrl;
	private String projectUrl;
	private String portalApp;
	private String soapUrl;
	private String topMenuUrl;

	public String getFileDownloadDir() {
		return fileDownloadDir;
	}

	public void setFileDownloadDir(String fileDownloadDir) {
		this.fileDownloadDir = fileDownloadDir;
	}

	public String getPublicScriptUploadDir() {
		return publicScriptUploadDir;
	}

	public void setPublicScriptUploadDir(String publicScriptUploadDir) {
		this.publicScriptUploadDir = publicScriptUploadDir;
	}

	public String getHqlScriptUploadDir() {
		return hqlScriptUploadDir;
	}

	public void setHqlScriptUploadDir(String hqlScriptUploadDir) {
		this.hqlScriptUploadDir = hqlScriptUploadDir;
	}

	public String getControlFileDir() {
		return controlFileDir;
	}

	public void setControlFileDir(String controlFileDir) {
		this.controlFileDir = controlFileDir;
	}

	public String getExecFileLnDir() {
		return execFileLnDir;
	}

	public void setExecFileLnDir(String execFileLnDir) {
		this.execFileLnDir = execFileLnDir;
	}

	public String getCreateLnFlag() {
		return createLnFlag;
	}

	public void setCreateLnFlag(String createLnFlag) {
		this.createLnFlag = createLnFlag;
	}

	public String getConfigExportDir() {
		return configExportDir;
	}

	public void setConfigExportDir(String configExportDir) {
		this.configExportDir = configExportDir;
	}

	public String getCasServiceUrl() {
		return casServiceUrl;
	}

	public void setCasServiceUrl(String casServiceUrl) {
		this.casServiceUrl = casServiceUrl;
	}

	public String getProjectUrl() {
		return projectUrl;
	}

	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	public String getPortalApp() {
		return portalApp;
	}

	public void setPortalApp(String portalApp) {
		this.portalApp = portalApp;
	}

	public String getSoapUrl() {
		return soapUrl;
	}

	public void setSoapUrl(String soapUrl) {
		this.soapUrl = soapUrl;
	}

	public String getTopMenuUrl() {
		return topMenuUrl;
	}

	public void setTopMenuUrl(String topMenuUrl) {
		this.topMenuUrl = topMenuUrl;
	}
}
