import api, { unwrap } from './client';

export const listResource = async (path, params = {}) => unwrap(await api.get(path, { params }));
export const getResource = async (path, id) => unwrap(await api.get(`${path}/${id}`));
export const createResource = async (path, payload) => unwrap(await api.post(path, payload));
export const updateResource = async (path, id, payload) => unwrap(await api.put(`${path}/${id}`, payload));
export const deleteResource = async (path, id) => api.delete(`${path}/${id}`);
export const uploadFile = async (payload) => unwrap(await api.post('/files/upload', payload, {
  headers: {
    'Content-Type': 'multipart/form-data'
  }
}));

export const downloadCsv = async (path) => {
  const response = await api.get(path, { responseType: 'blob' });
  return response.data;
};

export const authApi = {
  login: async (payload) => unwrap(await api.post('/auth/login', payload)),
  register: async (payload) => unwrap(await api.post('/auth/register', payload))
};
