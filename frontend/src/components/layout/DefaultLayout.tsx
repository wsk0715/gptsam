import Body from './body/Body';
import Header from './header/Header';
import Footer from './footer/Footer';

interface LayoutProps {
	children: React.ReactNode;
}

export default function Layout({ children }: LayoutProps) {
	return (
		<div className="flex flex-col min-h-screen">
			<Header />
			<Body>{children}</Body>
			<Footer />
		</div>
	);
}
