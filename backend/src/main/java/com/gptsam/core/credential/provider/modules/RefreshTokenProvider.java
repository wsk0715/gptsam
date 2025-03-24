package com.gptsam.core.credential.provider.modules;

import com.gptsam.core.credential.domain.Token;
import com.gptsam.core.credential.provider.properties.TokenProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TokenProperties.class)
public class RefreshTokenProvider {

	private final TokenProperties tokenProperties;
	private final TokenParser tokenParser;


	/**
	 * 사용자 id를 이용해 리프레시 토큰을 발급한다.
	 */
	public Token generateRefreshToken(Long userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + tokenProperties.refreshExpirationMs());

		return Token.of(
				Jwts.builder()
					.setSubject(String.valueOf(userId))
					.setIssuedAt(now)
					.setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS256, tokenProperties.jwtSecretKey().getBytes())
					.compact()
		);
	}

	/**
	 * 리프레시 토큰 만료가 가까우면 새로운 리프레시 토큰을 반환하고, 아나라면 기존 토큰을 반환한다.
	 */
	public Token updateRefreshToken(Token refreshToken) {
		if (shouldUpdateRefreshToken(refreshToken)) {
			return generateRefreshToken(tokenParser.getUserId(refreshToken));
		}
		return refreshToken;
	}

	private boolean shouldUpdateRefreshToken(Token refreshToken) {
		Date expiryDate = tokenParser.getClaims(refreshToken).getExpiration();
		Date now = new Date();

		long timeToExpiry = expiryDate.getTime() - now.getTime();

		// 만료 시간의 20% 이하로 남았으면 갱신 필요
		return timeToExpiry < (tokenProperties.refreshExpirationMs() * 0.2);
	}

}

