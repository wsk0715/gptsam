import type { Metadata } from 'next';
import localFont from 'next/font/local';
import './globals.css';
import Layout from '@/components/layout/DefaultLayout';

const geistSans = localFont({
	src: './fonts/GeistVF.woff',
	variable: '--font-geist-sans',
	weight: '100 900',
});
const geistMono = localFont({
	src: './fonts/GeistMonoVF.woff',
	variable: '--font-geist-mono',
	weight: '100 900',
});

export const metadata: Metadata = {
	title: 'GptSam',
	description: 'GptSam Client',
};

export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	return (
		<html lang="ko">
			<body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
				<Layout>{children}</Layout>
			</body>
		</html>
	);
}
