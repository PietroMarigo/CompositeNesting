import React from 'react';

/** Placement returned from the nesting backend. */
export interface Placement {
  id: string;
  x: number;
  y: number;
  rotation: number;
}

/** Specification of the sheet used for nesting. */
export interface SheetSpec {
  width: number;
  height: number;
}

/** Nesting result holding all placements and sheet info. */
export interface NestingResult {
  nestedParts: Placement[];
  sheet: SheetSpec;
}

interface PreviewProps {
  layout: NestingResult;
}

/**
 * Renders a simple SVG preview of the nested layout.
 * Each placement is currently drawn as a 1x1 rectangle at its origin.
 */
const NestingPreview: React.FC<PreviewProps> = ({ layout }) => {
  const { sheet, nestedParts } = layout;
  // Scale the preview to a fixed width while preserving aspect ratio
  const previewWidth = 400;
  const previewHeight = (sheet.height / sheet.width) * previewWidth;

  return (
    <svg
      width={previewWidth}
      height={previewHeight}
      viewBox={`0 0 ${sheet.width} ${sheet.height}`}
      style={{ border: '1px solid #ccc' }}
    >
      {/* Outline of the sheet */}
      <rect
        x={0}
        y={0}
        width={sheet.width}
        height={sheet.height}
        fill="none"
        stroke="#000"
      />
      {/* Placements rendered as small rectangles */}
      {nestedParts.map((p, i) => (
        <rect
          key={i}
          x={p.x}
          y={p.y}
          width={1}
          height={1}
          fill="rgba(0,0,255,0.5)"
        />
      ))}
    </svg>
  );
};

export default NestingPreview;
