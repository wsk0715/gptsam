package com.gptsam.core.credential.provider;

import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.secret-key}")
	private String jwtSecretKey;

	@Value("${security.jwt.expiration-ms}")
	private long expirationMs;

	@Value("${security.jwt.refresh-expiration-ms}")
	private long refreshExpirationMs;


	public String generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				   .setSubject(String.valueOf(user.getId()))
				   .claim("nickname", user.getNickname())
				   .claim("role", "USER")
				   .setIssuedAt(now)
				   .setExpiration(expiryDate)
				   .signWith(SignatureAlgorithm.HS256, jwtSecretKey.getBytes())
				   .compact();
	}

	public String refreshAccessToken(User loginUser, String refreshToken) {
		validateToken(refreshToken);
		return generateToken(loginUser);
	}

	public String generateRefreshToken(Long userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshExpirationMs);

		return Jwts.builder()
				   .setSubject(String.valueOf(userId))
				   .setIssuedAt(now)
				   .setExpiration(expiryDate)
				   .signWith(SignatureAlgorithm.HS256, jwtSecretKey.getBytes())
				   .compact();
	}

	public String updateRefreshToken(String refreshToken) {
		if (shouldUpdateRefreshToken(refreshToken)) {
			return generateRefreshToken(getUserId(refreshToken));
		}
		return refreshToken;
	}

	private boolean shouldUpdateRefreshToken(String refreshToken) {
		Date expiryDate = getClaims(refreshToken).getExpiration();
		Date now = new Date();

		long timeToExpiry = expiryDate.getTime() - now.getTime();

		// 만료 시간의 20% 이하로 남았으면 갱신 필요
		return timeToExpiry < (refreshExpirationMs * 0.2);
	}

	public long getUserId(String token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	public String getNickname(String token) {
		return getClaims(token).get("nickname", String.class);
	}

	public String getRole(String token) {
		return getClaims(token).get("role", String.class);
	}

	public void validateToken(String token) {
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

	private void checkExpiry(String token) {
		Date expiryDate = getClaims(token).getExpiration();
		Date now = new Date();

		// 토큰이 만료되었는지 확인
		if (expiryDate.before(now)) {
			throw new UnauthorizedException("만료된 토큰입니다.");
		}
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser()
					   .setSigningKey(jwtSecretKey.getBytes())
					   .parseClaimsJws(token)
					   .getBody();
		} catch (ExpiredJwtException e) {
			// 토큰 만료 여부는 따로 처리
			return e.getClaims();
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다.");
		}
	}

}

