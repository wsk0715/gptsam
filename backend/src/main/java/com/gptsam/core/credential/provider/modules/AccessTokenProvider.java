package com.gptsam.core.credential.provider.modules;

import com.gptsam.core.credential.domain.Token;
import com.gptsam.core.credential.provider.properties.TokenProperties;
import com.gptsam.core.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TokenProperties.class)
public class AccessTokenProvider {

	private final TokenProperties tokenProperties;
	private final TokenParser tokenParser;


	/**
	 * 사용자 정보를 이용해 액세스 토큰을 발급한다.
	 */
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
					.signWith(SignatureAlgorithm.HS256, tokenProperties.secretKey().getBytes())
					.compact()
		);
	}

	/**
	 * 리프레시 토큰을 이용해 액세스 토큰을 재발급한다.
	 */
	public Token refreshAccessToken(User loginUser, Token refreshToken) {
		tokenParser.validateToken(refreshToken);
		return generateToken(loginUser);
	}

}
