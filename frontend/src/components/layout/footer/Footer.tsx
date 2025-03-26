import React from 'react';
import FooterSection from './section/FooterSection';
import FlexGrid from '../../common/FlexGrid';
import Logo from '@/components/common/button/Logo';

export default function Footer() {
	const businessLinks = [
		{ label: 'About', href: '/about' },
		{ label: 'Contact', href: '/contact' },
	];

	const customerServiceLinks = [
		{ label: '1:1 문의', href: '/support' },
		{ label: 'FAQ', href: '/faq' },
	];

	const socialMediaLinks = [
		{ label: 'Facebook', href: 'https://facebook.com' },
		{ label: 'Twitter', href: 'https://twitter.com' },
		{ label: 'Instagram', href: 'https://instagram.com' },
	];

	return (
		<footer className="bg-gray-800 text-white">
			<FlexGrid cols={4}>
				<Logo size={1.5} isLink={false} />
				<></>
				<></>
				<></>
			</FlexGrid>
			<FlexGrid cols={4}>
				<></>
				<FooterSection title="Business" links={businessLinks} />
				<FooterSection title="Customer Service" links={customerServiceLinks} />
				<FooterSection title="Social Media" links={socialMediaLinks} />
			</FlexGrid>
			<Copyright />
		</footer>
	);
}

function Copyright() {
	return (
		<div className="text-center text-sm	mt-4 mb-8">
			<p>© {new Date().getFullYear()} GptSam. All rights reserved.</p>
		</div>
	);
}
