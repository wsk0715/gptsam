package com.gptsam.core.credential.provider;

import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.user.domain.User;
import io.jsonwebtoken.Claims;
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
			Jwts.parser()
				.setSigningKey(jwtSecretKey.getBytes())
				.parseClaimsJws(token);
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다.");
		}
	}

	private Claims getClaims(String token) {
		validateToken(token);

		try {
			return Jwts.parser()
					   .setSigningKey(jwtSecretKey.getBytes())
					   .parseClaimsJws(token)
					   .getBody();
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다.");
		}
	}

}

