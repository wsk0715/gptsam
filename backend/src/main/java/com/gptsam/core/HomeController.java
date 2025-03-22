package com.gptsam.core;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping
	public ResponseEntity<LocalDateTime> home() {
		return ResponseEntity.ok(LocalDateTime.now());
	}

}
