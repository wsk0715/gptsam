import Logo from '@/components/common/button/Logo';

export default function Header() {
	return (
		<header className="flex h-16 bg-white shadow-md items-center px-4 z-50">
			<Logo size={2} />
		</header>
	);
}
