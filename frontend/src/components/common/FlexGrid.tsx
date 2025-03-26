import React from 'react';

interface FlexGridProps {
	cols: number;
	children: React.ReactNode[];
	margin?: number;
	gap?: number;
}

const FlexGrid: React.FC<FlexGridProps> = ({ cols, children, margin = 6, gap = 2 }) => {
	const style = {
		marginLeft: `${margin}rem`,
		marginRight: `${margin}rem`,
		gap: `${gap}rem`,
	};

	return (
		<div className="flex flex-wrap mt-4" style={style}>
			{Array.from({ length: cols }, (_, idx) => (
				<div key={idx} className="flex-1 p-2">
					{children[idx]}
				</div>
			))}
		</div>
	);
};

export default FlexGrid;
