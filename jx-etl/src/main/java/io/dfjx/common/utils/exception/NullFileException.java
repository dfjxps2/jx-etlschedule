package io.dfjx.common.utils.exception;
/**
 * 空文件异常
 * @author bojiangzhou
 *
 */
public class NullFileException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		System.out.println("Exception: 上传的文件为空");
	}
}
