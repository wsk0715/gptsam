import { ReactNode } from 'react';
import Main from './main/Main';

interface BodyProps {
	children: ReactNode;
}

export default function Body({ children }: BodyProps) {
	return (
		<div className={'flex-1 p-6 flex'}>
			<Main>{children}</Main>
		</div>
	);
}
