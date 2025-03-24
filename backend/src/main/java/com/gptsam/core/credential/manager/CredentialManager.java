package com.gptsam.core.credential.manager;

import com.gptsam.core.credential.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 인증 정보를 관리하는 클래스를 정의하는 인터페이스
 */
public interface CredentialManager {

	void setCredential(Token token, HttpServletResponse httpServletResponse);

	Token getCredential(HttpServletRequest httpServletRequest);

	boolean hasCredential(HttpServletRequest httpServletRequest);

	void removeCredential(HttpServletResponse httpServletResponse);

}
