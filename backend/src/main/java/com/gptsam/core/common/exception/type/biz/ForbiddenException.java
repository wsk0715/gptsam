package com.gptsam.core.common.exception.type.biz;

import com.gptsam.core.common.exception.type.InternalServerException;

/**
 * 403 Forbidden
 */
public class ForbiddenException extends InternalServerException {

	private static final String DEFAULT_MESSAGE = "요청을 수행하기 위한 권한이 충분하지 않습니다.";

	public ForbiddenException() {
		super(DEFAULT_MESSAGE);
	}

	public ForbiddenException(String message) {
		super(message);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForbiddenException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
