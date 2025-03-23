package com.gptsam.core.exception.type.biz;


import com.gptsam.core.exception.type.InternalServerException;

/**
 * 400 Bad Request
 */
public class BadRequestException extends InternalServerException {

	private static final String DEFAULT_MESSAGE = "올바르지 않은 요청입니다.";

	public BadRequestException() {
		super(DEFAULT_MESSAGE);
	}

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadRequestException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
