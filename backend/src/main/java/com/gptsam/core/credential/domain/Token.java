package com.gptsam.core.credential.domain;

import com.gptsam.core.common.exception.type.sys.ArgumentNullException;
import lombok.Getter;

@Getter
public class Token {

	private final String value;

	public Token(String token) {
		validate(token);
		this.value = token;
	}

	public static Token of(String token) {
		return new Token(token);
	}

	private void validate(String token) {
		if (token == null || token.isEmpty()) {
			throw new ArgumentNullException();
		}
	}

}
