package com.gptsam.core.exception.type;

/**
 * 500 Internal Server Error
 */
public class InternalServerException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = "서버 내부 오류가 발생했습니다.";

	public InternalServerException() {
		super(DEFAULT_MESSAGE);
	}

	public InternalServerException(String message) {
		super(message);
	}

	public InternalServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalServerException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
