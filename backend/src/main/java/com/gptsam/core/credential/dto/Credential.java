package com.gptsam.core.credential.dto;

/**
 * 클라이언트 인증 정보를 담은 DTO
 */
public record Credential(
		String token,
		String refreshToken
) {

	public static Credential of(String token) {
		return new Credential(token, null);
	}

	public static Credential of(String token, String refreshToken) {
		return new Credential(token, refreshToken);
	}

}
