package io.dfjx.common.utils.exception;
/**
 * 文件格式异常
 * @author bojiangzhou
 *
 */
public class FileFormatException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		System.out.println("Exception: 上传文件的格式错误");
	}
}
