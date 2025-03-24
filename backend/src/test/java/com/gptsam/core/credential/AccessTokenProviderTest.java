package com.gptsam.core.credential;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gptsam.core.credential.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccessTokenProviderTest extends TokenProviderTest {

	@Override
	@BeforeEach
	void setUp() {
		super.setUp();
	}

	@Test
	@DisplayName("액세스 토큰이 정상적으로 생성된다.")
	void generateToken() {
		// given: 사용자 정보 설정
		when(user.getId()).thenReturn(expectedUserId);
		when(user.getNickname()).thenReturn(expectedUserNickname);

		// when: 토큰 생성
		Token token = accessTokenProvider.generateToken(user);

		// then: 토큰이 null이 아니고 비어있지 않은지 확인
		assertNotNull(token);
		assertNotNull(token.getValue());
	}

	@Test
	@DisplayName("리프레시 토큰을 이용해 액세스 토큰이 정상적으로 재발급된다.")
	void refreshAccessToken() {
		// given: 사용자 정보와 리프레시 토큰 설정
		when(user.getId()).thenReturn(expectedUserId);
		when(user.getNickname()).thenReturn(expectedUserNickname);

		// 유효한 리프레시 토큰 가정
		Token refreshToken = Token.of("valid-refresh-token");
		doNothing().when(mockTokenParser).validateToken(refreshToken);

		// when: 액세스 토큰 재발급
		Token newAccessToken = accessTokenProvider.refreshAccessToken(user, refreshToken);

		// then: 새로운 액세스 토큰이 null이 아니고 비어있지 않은지 확인
		assertNotNull(newAccessToken);
		assertNotNull(newAccessToken.getValue());

		// TokenParser의 validateToken 메소드가 호출되었는지 확인
		verify(mockTokenParser).validateToken(refreshToken);
	}

}
