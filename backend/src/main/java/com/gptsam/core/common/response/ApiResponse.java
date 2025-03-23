package com.gptsam.core.common.response;

import java.time.LocalDateTime;

/**
 * 클라이언트에 보내는 기본 응답 타입 DTO
 */
public record ApiResponse<T>(
		T result,               // 요청 결과
		String message,         // 결과 메시지
		LocalDateTime timestamp // 응답 일시
) {

	private final static String RESPONSE_MESSAGE_SUCCESS = "요청이 성공적으로 처리되었습니다.";


	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>(null, RESPONSE_MESSAGE_SUCCESS, LocalDateTime.now());
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(data, RESPONSE_MESSAGE_SUCCESS, LocalDateTime.now());
	}

	public static <T> ApiResponse<T> success(T data, String message) {
		return new ApiResponse<>(data, message, LocalDateTime.now());
	}

	public static <T> ApiResponse<T> error(String message) {
		return new ApiResponse<>(null, message, LocalDateTime.now());
	}

}
