import axios from 'axios';

const api = axios.create({
  baseURL:
    import.meta.env.VITE_API_BASE_URL ||
    'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } else if (config.headers?.Authorization) {
      delete config.headers.Authorization;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

export function getErrorMessage(error) {
  const data = error?.response?.data;

  if (data?.fields && typeof data.fields === 'object') {
    return Object.entries(data.fields)
      .map(([key, value]) => `${key}: ${value}`)
      .join(' · ');
  }

  return (
    data?.message ||
    data?.error ||
    error?.message ||
    'Something went wrong.'
  );
}

export default api;