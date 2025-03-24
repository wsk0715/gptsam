package com.gptsam.core.credential.dto;

import com.gptsam.core.credential.domain.Token;

/**
 * 클라이언트 인증 정보를 담은 DTO
 */
public record Credential(
		Token token,
		Token refreshToken
) {

	public static Credential of(Token token) {
		return new Credential(token, null);
	}
	
	public static Credential of(Token token, Token refreshToken) {
		return new Credential(token, refreshToken);
	}

}
