export interface LoginRequest {
  identifier: string;
  password: string;
  loginType: 'USERNAME' | 'EMAIL' | 'GOOGLE';
}

export interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  message: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
  avatarUrl?: string;
  role: 'STUDENT' | 'PARENT' | 'TEACHER' | 'ADMIN';
  createdAt: string;
  updatedAt: string;
}

export interface ApiResponse<T> {
  data?: T;
  message?: string;
  error?: string;
}
