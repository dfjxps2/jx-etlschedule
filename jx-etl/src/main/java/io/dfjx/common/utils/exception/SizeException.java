package io.dfjx.common.utils.exception;
/**
 * 文件大小异常
 * @author bojiangzhou
 *
 */
public class SizeException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		System.out.println("Exception: 上传文件超过限制大小");
	}
}
