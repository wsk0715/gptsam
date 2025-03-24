package com.gptsam.core.credential;

import com.gptsam.core.credential.provider.modules.AccessTokenProvider;
import com.gptsam.core.credential.provider.modules.RefreshTokenProvider;
import com.gptsam.core.credential.provider.modules.TokenParser;
import com.gptsam.core.credential.provider.properties.TokenProperties;
import com.gptsam.core.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

	@InjectMocks
	AccessTokenProvider accessTokenProvider;
	@InjectMocks
	RefreshTokenProvider refreshTokenProvider;
	@InjectMocks
	TokenParser tokenParser;

	@Mock
	TokenParser mockTokenParser;
	@Mock
	User user;

	final long expectedUserId = 1L;
	final String expectedUserNickname = "testUser";
	final String expectedRole = "USER";

	final String jwtSecretKey = "test-secret-key-for-jwt-token";
	final long expirationMs = 3600000; // 1시간
	final long refreshExpirationMs = 86400000; // 24시간
	private final TokenProperties tokenProperties = new TokenProperties(
			jwtSecretKey,
			expirationMs,
			refreshExpirationMs
	);

	@BeforeEach
	void setUp() {
		injectField(accessTokenProvider);
		injectField(refreshTokenProvider);
		injectField(tokenParser);
	}

	private void injectField(Object o) {
		ReflectionTestUtils.setField(o, "tokenProperties", tokenProperties);

		if (!(o instanceof TokenParser)) {
			ReflectionTestUtils.setField(o, "tokenParser", mockTokenParser);
		}
	}

}
