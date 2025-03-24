package com.gptsam.core.credential.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gptsam.core.common.exception.type.sys.ArgumentNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenTest {

	@Test
	@DisplayName("유효한 토큰 문자열로 Token 객체가 정상적으로 생성된다")
	void constructor() {
		// given
		String validTokenString = "valid.token.string";
		Token token = Token.of(validTokenString);

		// when & then
		assertNotNull(token);
		assertEquals(validTokenString, token.getValue());
	}

	@Test
	@DisplayName("토큰 생성 시 null 값이 들어가면 예외가 발생한다")
	void constructor_WithNullValue() {
		// when & then
		assertThrows(ArgumentNullException.class, () -> {
			Token.of(null);
		});
	}

	@Test
	@DisplayName("토큰 생성 시 빈 문자열이 들어가면 예외가 발생한다")
	void constructor_WithEmptyString() {
		// when & then
		assertThrows(ArgumentNullException.class, () -> {
			Token.of("");
		});
	}

}
