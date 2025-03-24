package com.gptsam.core.credential;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.credential.domain.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TokenParserTest extends TokenProviderTest {

	@Override
	@BeforeEach
	void setUp() {
		super.setUp();
	}

	@Test
	@DisplayName("토큰에서 사용자 ID를 정상적으로 추출한다.")
	void getUserId() {
		// given: 토큰 생성
		when(user.getId()).thenReturn(expectedUserId);
		when(user.getNickname()).thenReturn(expectedUserNickname);
		Token token = accessTokenProvider.generateToken(user);

		// when: 토큰에서 사용자 ID 추출
		long userId = tokenParser.getUserId(token);

		// then: 추출한 사용자 ID가 기대값과 일치하는지 확인
		assertEquals(expectedUserId, userId);
	}

	@Test
	@DisplayName("토큰에서 닉네임을 정상적으로 추출한다.")
	void getNickname() {
		// given: 토큰 생성
		when(user.getId()).thenReturn(expectedUserId);
		when(user.getNickname()).thenReturn(expectedUserNickname);
		Token token = accessTokenProvider.generateToken(user);

		// when: 토큰에서 닉네임 추출
		String nickname = tokenParser.getNickname(token);

		// then: 추출한 닉네임이 기대값과 일치하는지 확인
		assertEquals(expectedUserNickname, nickname);
	}

	@Test
	@DisplayName("토큰에서 역할을 정상적으로 추출한다.")
	void getRole() {
		// given: 토큰 생성
		when(user.getId()).thenReturn(expectedUserId);
		when(user.getNickname()).thenReturn(expectedUserNickname);
		Token token = accessTokenProvider.generateToken(user);

		// when: 토큰에서 역할 추출
		String role = tokenParser.getRole(token);

		// then: 추출한 역할이 기대값과 일치하는지 확인
		assertEquals(expectedRole, role);
	}

	@Test
	@DisplayName("토큰 검증 시 토큰이 유효하면 예외가 발생하지 않는다.")
	void validateToken() {
		// given: 유효한 토큰 생성
		Long userId = 1L;
		String nickname = "testUser";
		when(user.getId()).thenReturn(userId);
		when(user.getNickname()).thenReturn(nickname);
		Token token = accessTokenProvider.generateToken(user);

		// when & then: 유효한 토큰 검증 시 예외가 발생하지 않음
		assertDoesNotThrow(() -> tokenParser.validateToken(token));
	}

	@Test
	@DisplayName("토큰 검증 시 토큰이 null이면 예외가 발생한다.")
	void validateToken_tokenIsNull() {
		// given: null 토큰

		// when & then: null 토큰 검증 시 UnauthorizedException 발생
		assertThrows(UnauthorizedException.class, () -> {
			tokenParser.validateToken(null);
		});
	}

	@Test
	@DisplayName("토큰 검증 시 만료된 토큰은 예외가 발생한다.")
	void validateToken_tokenIsExpired() {
		// given: 만료된 토큰 생성
		Token expiredToken = Token.of(
				Jwts.builder()
					.setSubject("1")
					.claim("nickname", "testUser")
					.claim("role", "USER")
					.setIssuedAt(new Date(System.currentTimeMillis() - 2 * expirationMs))
					.setExpiration(new Date(System.currentTimeMillis() - expirationMs))
					.signWith(SignatureAlgorithm.HS256, jwtSecretKey.getBytes())
					.compact()
		);

		// when & then: 만료된 토큰 검증 시 UnauthorizedException 발생
		assertThrows(UnauthorizedException.class, () -> {
			tokenParser.validateToken(expiredToken);
		});
	}

	@Test
	@DisplayName("토큰 검증 시 잘못된 서명의 토큰은 예외가 발생한다.")
	void validateToken_invalidSignature() {
		// given: 잘못된 서명의 토큰 생성
		Token invalidToken = Token.of(
				Jwts.builder()
					.setSubject("1")
					.claim("nickname", "testUser")
					.claim("role", "USER")
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
					.signWith(SignatureAlgorithm.HS256, "wrong-secret-key".getBytes())
					.compact()
		);

		// when & then: 잘못된 서명의 토큰 검증 시 UnauthorizedException 발생
		assertThrows(UnauthorizedException.class, () -> {
			tokenParser.validateToken(invalidToken);
		});
	}

}
