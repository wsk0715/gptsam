import Link from 'next/link';

interface FooterSectionProps {
	title: string;
	links: { label: string; href: string }[];
}

export default function FooterSection({ title, links }: FooterSectionProps) {
	return (
		<div>
			<h5 className="text-lg text-bold">{title}</h5>
			<ul className="mt-2 text-sm">
				{links.map((link, index) => (
					<li key={index} className="mt-2">
						<Link href={link.href} className="hover:underline">
							{link.label}
						</Link>
					</li>
				))}
			</ul>
		</div>
	);
}
