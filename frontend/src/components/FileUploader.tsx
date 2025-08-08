import React from 'react';

interface FileUploaderProps {
  onFilesSelected: (files: FileList) => void;
}

/**
 * Simple input for selecting SVG or DXF files.
 */
const FileUploader: React.FC<FileUploaderProps> = ({ onFilesSelected }) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      onFilesSelected(e.target.files);
    }
  };

  return (
    <div>
      <input
        type="file"
        accept=".svg,.dxf"
        multiple
        onChange={handleChange}
      />
    </div>
  );
};

export default FileUploader;
