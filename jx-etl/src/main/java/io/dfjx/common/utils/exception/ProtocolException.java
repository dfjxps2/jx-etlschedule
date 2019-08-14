package io.dfjx.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolException extends Exception {

	private static Logger logger = LoggerFactory.getLogger(ProtocolException.class);


	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		logger.info("Exception: 客户端未使用MIME协议上传文件");
	}
}
