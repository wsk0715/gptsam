package com.gptsam.core.credential.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gptsam.core.credential.domain.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RefreshTokenProviderTest extends TokenProviderTest {

	@Override
	@BeforeEach
	void setUp() {
		super.setUp();
	}

	@Test
	@DisplayName("리프레시 토큰이 정상적으로 생성된다.")
	void generateRefreshToken() {
		// given: 사용자 ID 설정
		Long userId = 1L;

		// when: 리프레시 토큰 생성
		Token refreshToken = refreshTokenProvider.generateRefreshToken(userId);

		// then: 리프레시 토큰이 null이 아니고 비어있지 않은지 확인
		assertNotNull(refreshToken);
		assertNotNull(refreshToken.getValue());
	}

	@Test
	@DisplayName("리프레시 토큰 만료가 가까우면 새로운 리프레시 토큰을 반환한다")
	void updateRefreshToken_whenExpiringSoon() {
		// given: 만료가 임박한 리프레시 토큰 설정
		Token oldRefreshToken = Token.of("expiring-refresh-token");
		// 만료 시간이 임박했다고 가정 (shouldUpdateRefreshToken이 true 반환)
		Date expiryDate = new Date(System.currentTimeMillis() + (refreshExpirationMs / 10)); // 만료 시간의 10%만 남음
		Claims claims = Jwts.claims().setSubject(String.valueOf(expectedUserId)).setExpiration(expiryDate);

		when(mockTokenParser.getUserId(oldRefreshToken)).thenReturn(expectedUserId);
		when(mockTokenParser.getClaims(oldRefreshToken)).thenReturn(claims);

		// when: 리프레시 토큰 업데이트
		Token newRefreshToken = refreshTokenProvider.updateRefreshToken(oldRefreshToken);

		// then: 새로운 리프레시 토큰이 반환되었는지 확인
		assertNotNull(newRefreshToken);
		assertNotEquals(oldRefreshToken.getValue(), newRefreshToken.getValue());

		// getUserId 메소드가 호출되었는지 확인
		verify(mockTokenParser).getUserId(oldRefreshToken);
		verify(mockTokenParser).getClaims(oldRefreshToken);
	}

	@Test
	@DisplayName("리프레시 토큰 만료가 멀면 기존 리프레시 토큰을 그대로 반환한다")
	void updateRefreshToken_whenNotExpiringSoon() {
		// given: 만료가 멀지 않은 리프레시 토큰 설정
		Token refreshToken = Token.of("valid-refresh-token");

		// 만료 시간이 충분히 남았다고 가정 (shouldUpdateRefreshToken이 false 반환)
		Date expiryDate = new Date((long) (System.currentTimeMillis() + (refreshExpirationMs * 0.9))); // 만료 시간의 90%가 남음
		Claims claims = Jwts.claims().setSubject(String.valueOf(expectedUserId)).setExpiration(expiryDate);

		when(mockTokenParser.getClaims(refreshToken)).thenReturn(claims);

		// when: 리프레시 토큰 업데이트
		Token resultToken = refreshTokenProvider.updateRefreshToken(refreshToken);

		// then: 기존 리프레시 토큰이 그대로 반환되었는지 확인
		assertNotNull(resultToken);
		assertEquals(refreshToken, resultToken);

		// getUserId 메소드가 호출되지 않았는지 확인 (기존 토큰을 반환하므로)
		verify(mockTokenParser, never()).getUserId(refreshToken);
		verify(mockTokenParser).getClaims(refreshToken);
	}

}
