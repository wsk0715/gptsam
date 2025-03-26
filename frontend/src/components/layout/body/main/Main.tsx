interface MainProps {
	children: React.ReactNode;
}

export default function Main({ children }: MainProps) {
	return <main className="flex-1 flex flex-col">{children}</main>;
}
