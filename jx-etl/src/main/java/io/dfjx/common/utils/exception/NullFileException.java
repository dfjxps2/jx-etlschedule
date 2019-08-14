package io.dfjx.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空文件异常
 * @author bojiangzhou
 *
 */
public class NullFileException extends Exception {

	private static Logger logger = LoggerFactory.getLogger(NullFileException.class);

	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		logger.info("Exception: 上传的文件为空");
	}
}
