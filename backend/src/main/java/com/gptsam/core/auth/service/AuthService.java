package com.gptsam.core.auth.service;

import com.gptsam.core.credential.dto.Credential;
import com.gptsam.core.credential.manager.CredentialManager;
import com.gptsam.core.credential.provider.JwtTokenProvider;
import com.gptsam.core.user.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final CredentialManager credentialManager;
	private final JwtTokenProvider tokenProvider;


	public Credential setCredential(User user, HttpServletResponse response) {
		String token = tokenProvider.generateToken(user);
		Credential credential = new Credential(token);

		credentialManager.setCredential(credential, response);
		return credential;
	}

}
