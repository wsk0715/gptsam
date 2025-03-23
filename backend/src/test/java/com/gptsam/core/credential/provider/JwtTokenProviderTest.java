package com.gptsam.core.credential.provider;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

	@InjectMocks
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private User user;

	private String jwtSecretKey = "test-secret-key-for-jwt-token";
	private long expirationMs = 3600000; // 1시간
	private long refreshExpirationMs = 86400000; // 24시간

	@BeforeEach
	void setUp() {
		assertNotNull(jwtTokenProvider);

		// 올바른 형식으로 setField 호출
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecretKey", jwtSecretKey);
		ReflectionTestUtils.setField(jwtTokenProvider, "expirationMs", expirationMs);
		ReflectionTestUtils.setField(jwtTokenProvider, "refreshExpirationMs", refreshExpirationMs);
	}

	@Test
	@DisplayName("액세스 토큰이 정상적으로 생성된다.")
	void generateToken() {
		// given: 사용자 정보 설정
		Long userId = 1L;
		String nickname = "testUser";
		when(user.getId()).thenReturn(userId);
		when(user.getNickname()).thenReturn(nickname);

		// when: 토큰 생성
		String token = jwtTokenProvider.generateToken(user);

		// then: 토큰이 null이 아니고 비어있지 않은지 확인
		assertNotNull(token);
		assertFalse(token.isEmpty());
	}

	@Test
	@DisplayName("리프레시 토큰이 정상적으로 생성된다.")
	void generateRefreshToken() {
		// given: 사용자 ID 설정
		Long userId = 1L;

		// when: 리프레시 토큰 생성
		String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

		// then: 리프레시 토큰이 null이 아니고 비어있지 않은지 확인
		assertNotNull(refreshToken);
		assertFalse(refreshToken.isEmpty());
	}

	@Test
	@DisplayName("토큰에서 사용자 ID를 정상적으로 추출한다.")
	void getUserId() {
		// given: 토큰 생성
		Long expectedUserId = 1L;
		String nickname = "testUser";
		when(user.getId()).thenReturn(expectedUserId);
		when(user.getNickname()).thenReturn(nickname);
		String token = jwtTokenProvider.generateToken(user);

		// when: 토큰에서 사용자 ID 추출
		long userId = jwtTokenProvider.getUserId(token);

		// then: 추출한 사용자 ID가 기대값과 일치하는지 확인
		assertEquals(expectedUserId, userId);
	}

	@Test
	@DisplayName("토큰에서 닉네임을 정상적으로 추출한다.")
	void getNickname() {
		// given: 토큰 생성
		Long userId = 1L;
		String expectedNickname = "testUser";
		when(user.getId()).thenReturn(userId);
		when(user.getNickname()).thenReturn(expectedNickname);
		String token = jwtTokenProvider.generateToken(user);

		// when: 토큰에서 닉네임 추출
		String nickname = jwtTokenProvider.getNickname(token);

		// then: 추출한 닉네임이 기대값과 일치하는지 확인
		assertEquals(expectedNickname, nickname);
	}

	@Test
	@DisplayName("토큰에서 역할을 정상적으로 추출한다.")
	void getRole() {
		// given: 토큰 생성
		Long userId = 1L;
		String nickname = "testUser";
		String expectedRole = "USER";
		when(user.getId()).thenReturn(userId);
		when(user.getNickname()).thenReturn(nickname);
		String token = jwtTokenProvider.generateToken(user);

		// when: 토큰에서 역할 추출
		String role = jwtTokenProvider.getRole(token);

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
		String token = jwtTokenProvider.generateToken(user);

		// when & then: 유효한 토큰 검증 시 예외가 발생하지 않음
		assertDoesNotThrow(() -> jwtTokenProvider.validateToken(token));
	}

	@Test
	@DisplayName("토큰 검증 시 토큰이 null이면 예외가 발생한다.")
	void validateToken_tokenIsNull() {
		// given: null 토큰

		// when & then: null 토큰 검증 시 UnauthorizedException 발생
		assertThrows(UnauthorizedException.class, () -> {
			jwtTokenProvider.validateToken(null);
		});
	}

	@Test
	@DisplayName("토큰 검증 시 만료된 토큰은 예외가 발생한다.")
	void validateToken_tokenIsExpired() {
		// given: 만료된 토큰 생성
		String expiredToken = Jwts.builder()
								  .setSubject("1")
								  .claim("nickname", "testUser")
								  .claim("role", "USER")
								  .setIssuedAt(new Date(System.currentTimeMillis() - 2 * expirationMs))
								  .setExpiration(new Date(System.currentTimeMillis() - expirationMs))
								  .signWith(SignatureAlgorithm.HS256, jwtSecretKey.getBytes())
								  .compact();

		// when & then: 만료된 토큰 검증 시 UnauthorizedException 발생
		assertThrows(UnauthorizedException.class, () -> {
			jwtTokenProvider.validateToken(expiredToken);
		});
	}

	@Test
	@DisplayName("토큰 검증 시 잘못된 서명의 토큰은 예외가 발생한다.")
	void validateToken_invalidSignature() {
		// given: 잘못된 서명의 토큰 생성
		String invalidToken = Jwts.builder()
								  .setSubject("1")
								  .claim("nickname", "testUser")
								  .claim("role", "USER")
								  .setIssuedAt(new Date())
								  .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
								  .signWith(SignatureAlgorithm.HS256, "wrong-secret-key".getBytes())
								  .compact();

		// when & then: 잘못된 서명의 토큰 검증 시 UnauthorizedException 발생
		assertThrows(UnauthorizedException.class, () -> {
			jwtTokenProvider.validateToken(invalidToken);
		});
	}

}
