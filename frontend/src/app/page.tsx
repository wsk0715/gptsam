'use client';

import { useEffect, useState } from 'react';
import { apiClient } from './api/apiClient';

export default function Home() {
	const [data, setData] = useState<string | null>(null);
	const [error, setError] = useState<string | null>(null);
	const [isLoading, setIsLoading] = useState<boolean>(true);

	useEffect(() => {
		const fetchData = async () => {
			try {
				const data = await apiClient.get<string>('/');
				console.log(data);
				setData(data);
				setError(null);
			} catch (error) {
				const errorMessage = error instanceof Error ? error.message : '알 수 없는 에러가 발생했습니다';
				setError(errorMessage);
				setData(null);
			} finally {
				setIsLoading(false);
			}
		};

		fetchData();
	}, []);

	if (isLoading) return <div>로딩 중...</div>;
	if (error) return <div>에러: {error}</div>;
	if (!data) return <div>데이터가 없습니다</div>;

	return (
		<div>
			<div>{data}</div>
		</div>
	);
}
