package com.gptsam.core.common.encryption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashEncryptUtilTest {

	@Test
	@DisplayName("문자열이 SHA-256 알고리즘으로 해시되어 Base64로 인코딩된다.")
	void hash() {
		// given: 해시할 문자열 준비
		String target = "testString";

		// when: 해시 메소드 호출
		String hashedString = HashEncryptUtil.hash(target);

		// then: 해시된 결과가 원본과 다르고, Base64 인코딩 형식인지 확인
		assertNotEquals(target, hashedString);
		assertTrue(isBase64(hashedString));
	}

	private boolean isBase64(String str) {
		try {
			java.util.Base64.getDecoder().decode(str);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Test
	@DisplayName("동일한 입력에 대해 항상 같은 해시값을 반환한다.")
	void hash_consistentOutput() {
		// given: 동일한 입력 문자열 준비
		String target = "sameInputString";

		// when: 같은 문자열을 두 번 해시
		String hashedString1 = HashEncryptUtil.hash(target);
		String hashedString2 = HashEncryptUtil.hash(target);

		// then: 두 해시 결과가 동일한지 확인
		assertEquals(hashedString1, hashedString2);
	}

	@Test
	@DisplayName("서로 다른 입력에 대해 다른 해시값을 반환한다.")
	void hash_differentInputs() {
		// given: 서로 다른 두 입력 문자열 준비
		String target1 = "firstInput";
		String target2 = "secondInput";

		// when: 두 문자열을 각각 해시
		String hashedString1 = HashEncryptUtil.hash(target1);
		String hashedString2 = HashEncryptUtil.hash(target2);

		// then: 두 해시 결과가 서로 다른지 확인
		assertNotEquals(hashedString1, hashedString2);
	}
	
}
