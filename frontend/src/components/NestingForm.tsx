import React, { useState } from 'react';
import FileUploader from './FileUploader';
import NestingParametersForm, { type NestingParameters } from './NestingParameters';
import api from '../services/api';
import NestingPreview, { type NestingResult } from '../views/NestingPreview';

/**
 * Combines file uploading and parameter inputs, posts to the backend
 * and shows the returned placements.
 */
const NestingForm: React.FC = () => {
  const [files, setFiles] = useState<FileList | null>(null);
  const [params, setParams] = useState<NestingParameters>({ spacing: 0, allowRotation: true });
  const [layout, setLayout] = useState<NestingResult | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!files || files.length === 0) {
      setError('Please select at least one file.');
      return;
    }

    const formData = new FormData();
    Array.from(files).forEach((file) => formData.append('files', file));
    // Backend expects a JSON "config" payload. Provide basic defaults for
    // fields not yet exposed in the UI.
    const config = {
      spacing: params.spacing,
      rotationStep: params.allowRotation ? 90 : 0,
      sheetWidth: 100,
      sheetHeight: 100,
      maxNoImprovement: 10,
    };
    formData.append('config', JSON.stringify(config));

    try {
      setLoading(true);
      const response = await api.post<NestingResult>('/nest', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setLayout(response.data);
      setError(null);
    } catch {
      setError('Nesting failed');
      setLayout(null);
    } finally {
      setLoading(false);
    }
  };

  const handleExport = async () => {
    if (!layout) return;
    try {
      const response = await api.post('/export', { format: 'svg', layout }, {
        responseType: 'blob',
      });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'nested_layout.svg');
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch {
      setError('Export failed');
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
      {layout && (
        <div>
          <NestingPreview layout={layout} />
          <button onClick={handleExport}>Export</button>
        </div>
      )}
    </div>
  );
};

export default NestingForm;
