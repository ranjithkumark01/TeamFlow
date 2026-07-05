import { useCallback, useEffect, useState } from 'react';
import { getErrorMessage } from './client';
import { listResource } from './resources';

export default function useApiPage(path, initialParams = {}) {
  const [page, setPage] = useState(0);
  const [data, setData] = useState({ content: [], page: 0, size: 20, totalElements: 0, totalPages: 0, last: true });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const response = await listResource(path, { page, size: 20, ...initialParams });
      setData(response);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, [path, page, JSON.stringify(initialParams)]);

  useEffect(() => {
    load();
  }, [load]);

  return {
    ...data,
    loading,
    error,
    page,
    setPage,
    reload: load
  };
}

