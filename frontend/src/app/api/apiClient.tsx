'use client';

import api from './axiosConfig';

// 응답 body 타입 정의
export type ApiResponse<T = null> = {
	result: T;
	message: string;
	timestamp: string;
};

class ApiClient {
	private readonly apiEndPoint = ''; // api 기본 엔드포인트

	// GET 요청
	async get<T = null>(url: string) {
		try {
			const response = await api.get<ApiResponse<T>>(this.apiEndPoint + url);
			return response.data;
		} catch (err) {
			if (err instanceof Error) {
				throw new Error(`요청 처리 중 에러가 발생했습니다: ${err.message}`);
			}
			throw new Error('요청 처리 중 예외가 발생했습니다.');
		}
	}
}

// 인스턴스 생성 및 내보내기
export const apiClient = new ApiClient();
