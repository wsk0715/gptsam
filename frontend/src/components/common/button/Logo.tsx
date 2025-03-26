import Link from 'next/link';

interface LogoProps {
	size: number;
	isLink?: boolean;
}

export default function Logo({ size, isLink = true }: LogoProps) {
	const style = {
		fontSize: `${size}rem`,
	};

	return (
		<>
			{isLink ? (
				<Link href="/" className={`font-bold`} style={style}>
					GptSam
				</Link>
			) : (
				<div className={`font-bold`} style={style}>
					GptSam
				</div>
			)}
		</>
	);
}
