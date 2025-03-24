package com.gptsam.core.credential.service;

import com.gptsam.core.credential.domain.Token;
import com.gptsam.core.credential.dto.Credential;
import com.gptsam.core.credential.manager.cookie.CookieCredentialManager;
import com.gptsam.core.credential.manager.header.HeaderCredentialManager;
import com.gptsam.core.credential.provider.TokenProvider;
import com.gptsam.core.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialService {

	private final HeaderCredentialManager headerCredentialManager;
	private final CookieCredentialManager cookieCredentialManager;
	private final TokenProvider tokenProvider;


	public Credential setCredential(User user, HttpServletResponse response) {
		Token accessToken = tokenProvider.generateToken(user);
		Token refreshToken = tokenProvider.generateRefreshToken(user.getId());

		// 토큰 설정
		headerCredentialManager.setCredential(accessToken, response);
		cookieCredentialManager.setCredential(refreshToken, response);
		return Credential.of(accessToken, refreshToken);
	}

	public Credential refreshCredential(User loginUser, HttpServletRequest request, HttpServletResponse response) {
		// 리프레시 토큰 갱신(필요시)
		Token refreshToken = cookieCredentialManager.getCredential(request);
		refreshToken = tokenProvider.updateRefreshToken(refreshToken);
		// 액세스토큰 갱신
		Token accessToken = tokenProvider.refreshAccessToken(loginUser, refreshToken);

		// 토큰 설정
		headerCredentialManager.setCredential(accessToken, response);
		cookieCredentialManager.setCredential(refreshToken, response);
		return Credential.of(accessToken, refreshToken);
	}

	public Token getCredential(HttpServletRequest request) {
		return headerCredentialManager.getCredential(request);
	}

	public Long getUserIdFromToken(Token token) {
		return tokenProvider.getUserId(token);
	}

	public String getUserNicknameFromToken(Token token) {
		return tokenProvider.getNickname(token);
	}

	public void removeCredential(HttpServletResponse response) {
		headerCredentialManager.removeCredential(response);
		cookieCredentialManager.removeCredential(response);
	}

}
