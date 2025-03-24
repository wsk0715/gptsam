package com.gptsam.core.common.encryption;

import com.gptsam.core.common.exception.type.InternalServerException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashEncryptUtil {

	private static final String HASH_ALGORITHM = "SHA256";

	/**
	 * 단방향 해싱 함수(SHA-256)
	 */
	public static String hash(String target) {
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			byte[] hash = digest.digest(target.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new InternalServerException(HASH_ALGORITHM + " 해싱 실패", e);
		}
	}

}
