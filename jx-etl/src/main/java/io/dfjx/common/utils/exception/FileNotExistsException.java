package io.dfjx.common.utils.exception;
/**
 * 文件不存在异常
 * @author bojiangzhou
 *
 */
public class FileNotExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		System.out.println("Exception: 下载目录不存在");
	}
}
