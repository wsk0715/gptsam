package com.gptsam.core.common.exception.type.sys;

public class ArgumentNullException extends IllegalStateException {

	private static final String DEFAULT_MESSAGE = "전달받은 인자가 비어있습니다.";

	public ArgumentNullException() {
		super(DEFAULT_MESSAGE);
	}

	public ArgumentNullException(final String message) {
		super(message);
	}

	public ArgumentNullException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ArgumentNullException(final Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
