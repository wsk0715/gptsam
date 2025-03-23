package com.gptsam.core.auth.service;

import com.gptsam.core.credential.dto.Credential;
import com.gptsam.core.credential.manager.cookie.CookieCredentialManager;
import com.gptsam.core.credential.manager.header.HeaderCredentialManager;
import com.gptsam.core.credential.provider.JwtTokenProvider;
import com.gptsam.core.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final HeaderCredentialManager headerCredentialManager;
	private final CookieCredentialManager cookieCredentialManager;
	private final JwtTokenProvider tokenProvider;


	public Credential setCredential(User user, HttpServletResponse response) {
		String token = tokenProvider.generateToken(user);
		String refreshToken = tokenProvider.generateRefreshToken(user.getId());
		Credential credential = Credential.of(token, refreshToken);

		headerCredentialManager.setCredential(credential, response);
		cookieCredentialManager.setCredential(credential, response);
		return credential;
	}

	public Credential refreshCredential(User loginUser, Credential credential, HttpServletResponse response) {
		String refreshToken = credential.refreshToken();
		String accessToken = tokenProvider.refreshAccessToken(loginUser, refreshToken);
		refreshToken = tokenProvider.updateRefreshToken(refreshToken);

		headerCredentialManager.setCredential(accessToken, response);
		cookieCredentialManager.setCredential(refreshToken, response);
		return Credential.of(accessToken, refreshToken);
	}

	public void removeCredential(HttpServletResponse response) {
		headerCredentialManager.removeCredential(response);
		cookieCredentialManager.removeCredential(response);
	}

	public String getCredential(HttpServletRequest request) {
		return headerCredentialManager.getCredential(request);
	}

	public Long getUserIdFromToken(String token) {
		return tokenProvider.getUserId(token);
	}

	public String getUserNicknameFromToken(String token) {
		return tokenProvider.getNickname(token);
	}

}
