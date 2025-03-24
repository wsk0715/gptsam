package com.gptsam.core.common.exception.type.biz;

import com.gptsam.core.common.exception.type.InternalServerException;

/**
 * 401 Unauthorized
 */
public class UnauthorizedException extends InternalServerException {

	private static final String DEFAULT_MESSAGE = "요청을 수행하기 위해서 인증이 필요합니다.";

	public UnauthorizedException() {
		super(DEFAULT_MESSAGE);
	}

	public UnauthorizedException(final String message) {
		super(message);
	}

	public UnauthorizedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedException(final Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
