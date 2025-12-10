import React, { useState } from 'react';
import { useAuth } from '../AuthContext';
import { authApi } from '../api';
import type { LoginRequest } from '../types';

const Login: React.FC = () => {
  const [formData, setFormData] = useState<LoginRequest>({
    identifier: '',
    password: '',
    loginType: 'USERNAME',
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>('');

  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      // Login first
      const loginResponse = await authApi.login(formData);

      // Get user details to check role
      const user = await authApi.getUser(loginResponse.userId);

      // Check if user is admin
      if (user.role !== 'ADMIN') {
        throw new Error('Access denied. Admin privileges required.');
      }

      // Login successful
      login(loginResponse.token, user);
    } catch (err) {
      console.error('Login error:', err);
      setError(err instanceof Error ? err.message : 'Login failed');
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-1/4 space-y-8 px-16 pb-48 pt-24 shadow shadow-lg rounded-lg bg-white">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            管理员登录
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            登录以访问管理面板
          </p>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md shadow-sm -space-y-px">
            <div>
              <label htmlFor="loginType" className="sr-only">
                Login Type
              </label>
              <select
                id="loginType"
                name="loginType"
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                value={formData.loginType}
                onChange={handleInputChange}
              >
                <option value="USERNAME">用户名登录</option>
                <option value="EMAIL">邮箱登录</option>
              </select>
            </div>
            <div>
              <label htmlFor="identifier" className="sr-only">
                {formData.loginType === 'EMAIL' ? 'Email' : 'Username'}
              </label>
              <input
                id="identifier"
                name="identifier"
                type={formData.loginType === 'EMAIL' ? 'email' : 'text'}
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder={formData.loginType === 'EMAIL' ? '邮箱地址' : '用户名'}
                value={formData.identifier}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder="密码"
                value={formData.password}
                onChange={handleInputChange}
              />
            </div>
          </div>

          {error && (
            <div className="text-red-600 text-sm text-center">
              {error}
            </div>
          )}

          <div>
            <button
              type="submit"
              disabled={isLoading}
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? '登录中...' : '登录'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
