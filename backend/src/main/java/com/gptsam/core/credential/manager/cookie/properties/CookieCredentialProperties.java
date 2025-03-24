package com.gptsam.core.credential.manager.cookie.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.cookie")
public record CookieCredentialProperties(
		int maxAge,
		boolean isSecure,
		boolean httpOnly,
		String sameSite
) {

	public String cookieName() {
		return "refreshToken";
	}

}
