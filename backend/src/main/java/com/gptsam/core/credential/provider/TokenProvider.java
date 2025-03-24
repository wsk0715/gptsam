package com.gptsam.core.credential.provider;

import com.gptsam.core.credential.domain.Token;
import com.gptsam.core.credential.provider.modules.AccessTokenProvider;
import com.gptsam.core.credential.provider.modules.RefreshTokenProvider;
import com.gptsam.core.credential.provider.modules.TokenParser;
import com.gptsam.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private final AccessTokenProvider accessTokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;
	private final TokenParser tokenParser;


	public Token getToken(User user) {
		return accessTokenProvider.generateToken(user);
	}

	public Token updateToken(User loginUser, Token refreshToken) {
		return accessTokenProvider.refreshAccessToken(loginUser, refreshToken);
	}

	public Token getRefreshToken(Long userId) {
		return refreshTokenProvider.generateRefreshToken(userId);
	}

	public Token updateRefreshToken(Token refreshToken) {
		return refreshTokenProvider.updateRefreshToken(refreshToken);
	}

	public long getUserId(Token token) {
		return tokenParser.getUserId(token);
	}

	public String getNickname(Token token) {
		return tokenParser.getNickname(token);
	}

	public String getRole(Token token) {
		return tokenParser.getRole(token);
	}

}

