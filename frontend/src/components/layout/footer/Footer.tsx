import React from 'react';
import Logo from '@/components/common/button/Logo';

export default function Footer() {
	return (
		<footer className="p-6 bg-gray-800 text-white">
			<Logo size={1.5} isLink={false} />
			<Copyright />
		</footer>
	);
}

function Copyright() {
	return (
		<div className="text-center text-sm	my-4">
			<p>© {new Date().getFullYear()} GptSam. All rights reserved.</p>
		</div>
	);
}
