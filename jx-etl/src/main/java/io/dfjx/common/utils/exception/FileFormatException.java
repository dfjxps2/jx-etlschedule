package io.dfjx.common.utils.exception;

import io.dfjx.modules.etl.util.ExcelData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件格式异常
 * @author bojiangzhou
 *
 */
public class FileFormatException extends Exception {

	private static Logger logger = LoggerFactory.getLogger(FileFormatException.class);

	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		logger.info("Exception: 上传文件的格式错误");
	}
}
