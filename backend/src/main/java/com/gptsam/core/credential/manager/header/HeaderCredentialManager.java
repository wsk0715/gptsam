package com.gptsam.core.credential.manager.header;


import com.gptsam.core.common.exception.type.biz.UnauthorizedException;
import com.gptsam.core.credential.dto.Credential;
import com.gptsam.core.credential.manager.CredentialManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HeaderCredentialManager implements CredentialManager {

	private static final String AUTHORIZATION_TYPE = "Bearer";


	/**
	 * 헤더에 인증 정보를 설정한다.
	 */
	@Override
	public void setCredential(Credential credential, HttpServletResponse response) {
		response.setHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE + " " + credential.token());
	}

	/**
	 * 헤더에서 인증 정보를 불러온다.
	 */
	@Override
	public String getCredential(HttpServletRequest request) {
		if (!hasCredential(request)) {
			throw new UnauthorizedException("헤더에 인증 정보가 존재하지 않습니다.");
		}

		String[] authorization = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ");
		if (authorization.length < 2 || !AUTHORIZATION_TYPE.equals(authorization[0])) {
			throw new UnauthorizedException("헤더의 인증 정보가 잘못되었습니다.");
		}

		return authorization[1];
	}

	/**
	 * 헤더에 인증 정보가 존재하는지 확인한다.
	 */
	@Override
	public boolean hasCredential(HttpServletRequest httpServletRequest) {
		String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		return header != null && !header.isEmpty();
	}

	/**
	 * 헤더의 인증 정보를 제거한다.
	 */
	@Override
	public void removeCredential(HttpServletResponse response) {
		response.setHeader(HttpHeaders.AUTHORIZATION, "");
	}

}
