package io.dfjx.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件大小异常
 * @author bojiangzhou
 *
 */
public class SizeException extends Exception {

	private static Logger logger = LoggerFactory.getLogger(SizeException.class);


	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		logger.info("Exception: 上传文件超过限制大小");
	}
}
