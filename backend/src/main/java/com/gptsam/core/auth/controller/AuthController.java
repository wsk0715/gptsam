package com.gptsam.core.auth.controller;

import com.gptsam.core.auth.annotation.LoginUser;
import com.gptsam.core.auth.service.AuthService;
import com.gptsam.core.common.response.ApiResponse;
import com.gptsam.core.credential.dto.Credential;
import com.gptsam.core.user.domain.User;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Credential>> login(HttpServletResponse response) {
		// TODO: 실제 사용자 정보 사용하도록 로직 수정
		User tUser = new User(1L, "방가맨");
		Credential credential = authService.setCredential(tUser, response);

		return ResponseEntity.ok(ApiResponse.success(credential));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@Parameter(hidden = true) @LoginUser User loginUser,
													HttpServletResponse response) {
		// TODO: 로그아웃 시 액세스 토큰 블랙리스트 추가
		authService.removeCredential(response);

		return ResponseEntity.ok().build();
	}

}
