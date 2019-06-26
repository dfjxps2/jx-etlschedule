package io.dfjx.common.utils.fileload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.dfjx.common.utils.exception.FileNotExistsException;

/**
 * 下载文件
 * @author bojiangzhou
 *
 */
public class FileDownload {
	/**
	 * 下载时的默认文件目录
	 */
	private String filePath = null;
	/**
	 * 要下载文件的格式
	 */
	private List<String> fileFormat = new ArrayList<String>();
	/**
	 * HttpServletRequest
	 */
	private HttpServletRequest request;
	
	public FileDownload(HttpServletRequest request){
		this.request = request;
	}
	
	public FileDownload(String filePath, HttpServletRequest request){
		this.request = request;
		this.filePath = filePath;
	}
	
	/**
	 * 将下载列表绑定到域中
	 * 默认session
	 * @param var 域对象变量名
	 * @throws FileNotExistsException 
	 */
	public void bindDownloadFilesToScope(String var) throws FileNotExistsException{
		if(filePath == null){
			filePath = "D:/uploadFile";
		}
		if(!isFileExists(this.filePath)){
			throw new FileNotExistsException();
		}
		List<DownloadFile> list = new ArrayList<DownloadFile>();
		getDownloadFiles(filePath, list);
		request.getSession().setAttribute(var, list);
	}
	
	/**
	 * 获得下载目录下的所有文件
	 * @param filePath
	 * @param list 
	 */
	private void getDownloadFiles(String filePath, List<DownloadFile> list){
		File file = new File(filePath);
		if(file.isFile()){
			String uuidFileName = file.getName();
			if(isFormat(uuidFileName.substring(uuidFileName.lastIndexOf(".")+1))){
				DownloadFile df = new DownloadFile();
				df.setFileName(uuidFileName.substring(uuidFileName.indexOf("#")+1));
				df.setUuidFileName(uuidFileName);
				df.setFilePath(file.getPath());
				list.add(df);
			}
		} else{
			File[] childFiles = file.listFiles();
			for(File cf : childFiles){
				getDownloadFiles(cf.getPath(), list);
			}
		}
	}
	
	/**
	 * 下载文件
	 * @param var 下载列表的域变量名
	 * @param uuidFileName 客户端传来的文件的uuid名
	 * @param response
	 * @throws IOException
	 */
	public void downloadFile(String var, String uuidFileName, HttpServletResponse response) throws IOException{
		byte[] by = uuidFileName.getBytes("ISO-8859-1");
		uuidFileName = new String(by, "UTF-8");
		List<DownloadFile> files = (List<DownloadFile>) this.request.getSession().getAttribute(var);
		for(DownloadFile file : files){
			if(file.getUuidFileName().equals(uuidFileName)){
				InputStream is = new FileInputStream(file.getFilePath());
				OutputStream os = response.getOutputStream();
				response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(file.getFileName(), "UTF-8"));
				output(is, os);
				break;
			}
		}
	}
	
	public void output(InputStream is, OutputStream os) throws IOException{
		byte[] by = new byte[1024];
		int len = 0;
		while( (len = is.read(by)) > 0 ){
			os.write(by, 0, len);
		}
		is.close();
		os.close();
	}
	
	/**
	 * 判断文件的格式是否正确
	 * @param format 文件格式
	 * @return boolean
	 */
	private boolean isFormat(String format){
		if(fileFormat.size() == 0){
			return true;
		}
		for(String f : fileFormat){
			if(f.equalsIgnoreCase(format)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断文件目录是否存在
	 * @param filePath 文件目录
	 * @return boolean
	 */
	private boolean isFileExists(String filePath){
		boolean b = true;
		File file = new File(filePath);
		if(!file.exists()){
			b = false;
		}
		return b;
	}

	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * 设置下载路径
	 * @param filePath
	 * @throws FileNotExistsException
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * 获取允许上传文件的格式
	 * @return
	 */
	public String getFileFormat() {
		if(fileFormat.size() == 0){
			return "*";
		}
		String format = "";
		for(String s:fileFormat){
			format += ","+s;
		}
		format = format.substring(format.indexOf(",")+1);
		return format;
	}
	/**
	 * 设置上传文件的格式,多个文件格式则多次调用该方法进行设置
	 * @param fileFormat
	 */
	public void setFileFormat(String format) {
		this.fileFormat.add(format);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}
