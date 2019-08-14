package io.dfjx.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件不存在异常
 * @author bojiangzhou
 *
 */
public class FileNotExistsException extends Exception {

	private static Logger logger = LoggerFactory.getLogger(FileNotExistsException.class);

	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		logger.info("Exception: 下载目录不存在");
	}
}
