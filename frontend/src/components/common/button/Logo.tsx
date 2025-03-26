import Link from 'next/link';

interface LogoProps {
	size: number;
}

export default function Logo({ size }: LogoProps) {
	return (
		<Link href="/" className={`text-[${size}rem] font-bold`}>
			GptSam
		</Link>
	);
}
