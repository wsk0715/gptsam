package com.gptsam.core;

import com.gptsam.core.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping
	public ResponseEntity<ApiResponse<Void>> home() {
		return ResponseEntity.ok(ApiResponse.success());
	}

}
