import axios from 'axios';
import type { LoginRequest, LoginResponse, User } from './types';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authApi = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post('/auth/login', data);
    return response.data;
  },

  getUser: async (userId: number): Promise<User> => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
  },
};

export const userApi = {
  getUsers: async (): Promise<User[]> => {
    const response = await api.get('/users');
    return response.data;
  },

  updateUser: async (userId: number, data: Partial<User>): Promise<User> => {
    const response = await api.put(`/users/${userId}`, data);
    return response.data;
  },
};

export default api;
