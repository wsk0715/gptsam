package com.gptsam.core.credential.provider;

import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.credential.domain.Token;
import com.gptsam.core.credential.provider.properties.TokenProperties;
import com.gptsam.core.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TokenProperties.class)
public class TokenProvider {

	private final TokenProperties tokenProperties;


	public Token generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + tokenProperties.expirationMs());

		return Token.of(
				Jwts.builder()
					.setSubject(String.valueOf(user.getId()))
					.claim("nickname", user.getNickname())
					.claim("role", "USER")
					.setIssuedAt(now)
					.setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS256, tokenProperties.jwtSecretKey().getBytes())
					.compact()
		);
	}

	public Token refreshAccessToken(User loginUser, Token refreshToken) {
		validateToken(refreshToken);
		return generateToken(loginUser);
	}

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

	public Token updateRefreshToken(Token refreshToken) {
		if (shouldUpdateRefreshToken(refreshToken)) {
			return generateRefreshToken(getUserId(refreshToken));
		}
		return refreshToken;
	}

	private boolean shouldUpdateRefreshToken(Token refreshToken) {
		Date expiryDate = getClaims(refreshToken).getExpiration();
		Date now = new Date();

		long timeToExpiry = expiryDate.getTime() - now.getTime();

		// 만료 시간의 20% 이하로 남았으면 갱신 필요
		return timeToExpiry < (tokenProperties.refreshExpirationMs() * 0.2);
	}

	public long getUserId(Token token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	public String getNickname(Token token) {
		return getClaims(token).get("nickname", String.class);
	}

	public String getRole(Token token) {
		return getClaims(token).get("role", String.class);
	}

	public void validateToken(Token token) {
		if (token == null) {
			throw new UnauthorizedException("토큰이 존재하지 않습니다");
		}

		try {
			getClaims(token);
			checkExpiry(token);
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다.");
		}
	}

	private void checkExpiry(Token token) {
		Date expiryDate = getClaims(token).getExpiration();
		Date now = new Date();

		// 토큰이 만료되었는지 확인
		if (expiryDate.before(now)) {
			throw new UnauthorizedException("만료된 토큰입니다.");
		}
	}

	private Claims getClaims(Token token) {
		try {
			return Jwts.parser()
					   .setSigningKey(tokenProperties.jwtSecretKey().getBytes())
					   .parseClaimsJws(token.getValue())
					   .getBody();
		} catch (ExpiredJwtException e) {
			// 토큰 만료 여부는 따로 처리
			return e.getClaims();
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다.");
		}
	}

}

