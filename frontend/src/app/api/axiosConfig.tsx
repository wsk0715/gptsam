'use client';

import axios from 'axios';

const baseURL = process.env.NEXT_PUBLIC_API_URL;

const api = axios.create({
	baseURL,
	timeout: 5000, // 5초
	headers: {
		'Content-Type': 'application/json',
	},
});

// 응답 인터셉터
api.interceptors.response.use(
	(response) => {
		// 상태 코드가 200인 경우
		if (response.status === 200) {
			return response.data;
		}
	},
	(err) => {
		// 요청 설정 중 에러가 발생한 경우
		throw new Error(`요청 설정 중 에러가 발생했습니다: ${err}`);
	}
);

export default api;
