interface MainProps {
	children: React.ReactNode;
}

export default function Main({ children }: MainProps) {
	return <main>{children}</main>;
}
