package com.gptsam.core.credential.provider.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record TokenProperties(
		String secretKey,
		long expirationMs,
		long refreshExpirationMs
) {

}
