// 응답 body 타입 정의
export type ApiResponse<T = null> = {
	result: T;
	message: string;
	timestamp: string;
};
