package com.gptsam.core.common.exception.type.biz;

import com.gptsam.core.common.exception.type.InternalServerException;

/**
 * 404 Not Found
 */
public class NotFoundException extends InternalServerException {

	private static final String DEFAULT_MESSAGE = "요청에 대한 항목을 찾을 수 없습니다.";

	public NotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
