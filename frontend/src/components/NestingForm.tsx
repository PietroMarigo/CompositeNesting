import React, { useState } from 'react';
import FileUploader from './FileUploader';
import NestingParametersForm, { NestingParameters } from './NestingParameters';
import api from '../services/api';

/**
 * Combines file uploading and parameter inputs, posts to the backend
 * and shows the returned placements.
 */
const NestingForm: React.FC = () => {
  const [files, setFiles] = useState<FileList | null>(null);
  const [params, setParams] = useState<NestingParameters>({ spacing: 0, allowRotation: true });
  const [placements, setPlacements] = useState<unknown>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!files || files.length === 0) {
      setError('Please select at least one file.');
      return;
    }

    const formData = new FormData();
    Array.from(files).forEach((file) => formData.append('files', file));
    formData.append('spacing', String(params.spacing));
    formData.append('allowRotation', String(params.allowRotation));

    try {
      setLoading(true);
      const response = await api.post('/nest', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setPlacements(response.data);
      setError(null);
    } catch {
      setError('Nesting failed');
      setPlacements(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <FileUploader onFilesSelected={setFiles} />
      <NestingParametersForm onChange={setParams} />
      <button onClick={handleSubmit} disabled={loading}>
        {loading ? 'Nesting...' : 'Nest'}
      </button>
      {error && <div className="error">{error}</div>}
      {placements && (
        <pre>{JSON.stringify(placements, null, 2)}</pre>
      )}
    </div>
  );
};

export default NestingForm;
