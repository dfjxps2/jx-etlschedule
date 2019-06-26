package io.dfjx.common.utils.exception;

public class ProtocolException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		System.out.println("Exception: 客户端未使用MIME协议上传文件");
	}
}
