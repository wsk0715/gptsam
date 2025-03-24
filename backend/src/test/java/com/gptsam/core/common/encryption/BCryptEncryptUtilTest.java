package com.gptsam.core.common.encryption;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BCryptEncryptUtilTest {

	@Test
	@DisplayName("평문이 암호화되어 BCrypt 해시값이 생성된다.")
	void encode() {
		// given: 암호화할 평문 설정
		String plainText = "password1@";

		// when: 암호화 메소드 호출
		String hashedText = BCryptEncryptUtil.encode(plainText);

		// then: 암호화된 결과가 BCrypt 암호화 형식을 따르는지 확인
		assertNotEquals(plainText, hashedText);
		assertTrue(hashedText.startsWith("$2a$"));
	}

	@Test
	@DisplayName("동일한 평문이라도 매번 다른 해시값이 생성된다.")
	void encode_differentHashForSamePlaintext() {
		// given: 동일한 평문 준비
		String plainText = "samePassword";

		// when: 동일한 평문을 두 번 암호화
		String hashedText1 = BCryptEncryptUtil.encode(plainText);
		String hashedText2 = BCryptEncryptUtil.encode(plainText);

		// then: 두 해시값이 서로 다른지 확인
		assertNotEquals(hashedText1, hashedText2);

		// 두 해시값 모두 원래 평문과 일치하는지 확인
		assertTrue(BCryptEncryptUtil.isMatches(plainText, hashedText1));
		assertTrue(BCryptEncryptUtil.isMatches(plainText, hashedText2));
	}

	@Test
	@DisplayName("평문과 해시값이 일치하면 true를 반환한다.")
	void isMatches() {
		// given: 평문과 그에 해당하는 해시값 준비
		String plainText = "securePassword";
		String hashedText = BCryptEncryptUtil.encode(plainText);

		// when & then: 평문과 해시값이 일치하는지 확인
		assertTrue(BCryptEncryptUtil.isMatches(plainText, hashedText));
	}

	@Test
	@DisplayName("평문과 해시값이 일치하지 않으면 false를 반환한다.")
	void isMatches_wrongPassword() {
		// given: 평문과 다른 평문에 대한 해시값 준비
		String plainText = "correctPassword";
		String wrongPlainText = "wrongPassword";
		String hashedText = BCryptEncryptUtil.encode(plainText);

		// when & then: 잘못된 평문과 해시값이 일치하지 않는지 확인
		assertFalse(BCryptEncryptUtil.isMatches(wrongPlainText, hashedText));
	}

}
