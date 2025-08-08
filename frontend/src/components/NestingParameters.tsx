import React, { useState } from 'react';

export interface NestingParameters {
  spacing: number;
  allowRotation: boolean;
}

interface NestingParametersProps {
  onChange: (params: NestingParameters) => void;
}

/**
 * Form inputs for basic nesting parameters.
 */
const NestingParametersForm: React.FC<NestingParametersProps> = ({ onChange }) => {
  const [spacing, setSpacing] = useState(0);
  const [allowRotation, setAllowRotation] = useState(true);

  const updateSpacing = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseFloat(e.target.value);
    setSpacing(value);
    onChange({ spacing: value, allowRotation });
  };

  const updateRotation = (e: React.ChangeEvent<HTMLInputElement>) => {
    const checked = e.target.checked;
    setAllowRotation(checked);
    onChange({ spacing, allowRotation: checked });
  };

  return (
    <div>
      <label>
        Spacing:
        <input type="number" value={spacing} onChange={updateSpacing} />
      </label>
      <label>
        Allow Rotation:
        <input type="checkbox" checked={allowRotation} onChange={updateRotation} />
      </label>
    </div>
  );
};

export default NestingParametersForm;
