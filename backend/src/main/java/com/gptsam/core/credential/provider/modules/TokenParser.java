package com.gptsam.core.credential.provider.modules;

import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.credential.domain.Token;
import com.gptsam.core.credential.provider.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TokenProperties.class)
public class TokenParser {

	private final TokenProperties tokenProperties;


	public long getUserId(Token token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	public String getNickname(Token token) {
		return getClaims(token).get("nickname", String.class);
	}

	public String getRole(Token token) {
		return getClaims(token).get("role", String.class);
	}

	/**
	 * 토큰에 대해 유효성 검사를 수행한다.
	 */
	public void validateToken(Token token) {
		if (token == null) {
			throw new UnauthorizedException("토큰이 존재하지 않습니다");
		}

		try {
			getClaims(token);   // 토큰이 해석되는지 확인
			checkExpiry(token); // 토큰이 만료되었는지 확인
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

	/**
	 * 주어진 시크릿 키를 이용해 토큰을 해석한다.
	 */
	public Claims getClaims(Token token) {
		try {
			return Jwts.parser()
					   .setSigningKey(tokenProperties.secretKey().getBytes())
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

