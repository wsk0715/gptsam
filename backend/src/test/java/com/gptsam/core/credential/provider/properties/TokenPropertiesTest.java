package com.gptsam.core.credential.provider.properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gptsam.core.env.ActiveSpringProfileTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(TokenProperties.class)
class TokenPropertiesTest extends ActiveSpringProfileTest {

	@Autowired
	private TokenProperties tokenProperties;

	@Test
	@DisplayName("tokenProperties가 null이 아니어야 한다.")
	void tokenProperties_NotNull() {
		assertNotNull(tokenProperties);
	}

	@Test
	@DisplayName("secretKey 속성이 null이 아니어야 한다.")
	void secretKey_NotNull() {
		assertNotNull(tokenProperties.secretKey());
		assertFalse(tokenProperties.secretKey().isEmpty());
	}

	@Test
	@DisplayName("secretKey 속성이 비어있지 않아야 한다.")
	void secretKey_NotEmpty() {
		assertNotNull(tokenProperties.secretKey());
		assertFalse(tokenProperties.secretKey().isEmpty());
	}

	@Test
	@DisplayName("액세스 토큰 만료 시간이 양수여야 한다.")
	void expirationMs_IsPositive() {
		assertTrue(tokenProperties.expirationMs() > 0);
	}

	@Test
	@DisplayName("리프레시 토큰 만료 시간이 양수여야 한다.")
	void refreshExpirationMs_IsPositive() {
		assertTrue(tokenProperties.refreshExpirationMs() > 0);
	}

}
