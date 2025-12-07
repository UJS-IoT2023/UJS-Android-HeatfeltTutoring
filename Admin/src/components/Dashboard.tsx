import React, { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { userApi } from '../api';
import type { User } from '../types';

const Dashboard: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const { user, logout } = useAuth();

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const userList = await userApi.getUsers();
      setUsers(userList);
    } catch (err) {
      console.error('Failed to load users:', err);
      setError('Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-lg">加载中...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-gray-900">管理员面板</h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-700">欢迎, {user?.username}</span>
              <button
                onClick={handleLogout}
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                登出
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white shadow overflow-hidden sm:rounded-md">
            <div className="px-4 py-5 sm:px-6">
              <h2 className="text-lg leading-6 font-medium text-gray-900">
                用户管理
              </h2>
              <p className="mt-1 max-w-2xl text-sm text-gray-500">
                管理系统中的所有用户
              </p>
            </div>

            {error && (
              <div className="px-4 py-3 bg-red-50 border-l-4 border-red-400">
                <div className="text-red-700">{error}</div>
              </div>
            )}

            <ul className="divide-y divide-gray-200">
              {users.map((user) => (
                <li key={user.id} className="px-4 py-4 sm:px-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      {user.avatarUrl && (
                        <img
                          className="h-10 w-10 rounded-full mr-4"
                          src={`http://localhost:8080${user.avatarUrl}`}
                          alt={user.username}
                        />
                      )}
                      <div>
                        <div className="text-sm font-medium text-gray-900">
                          {user.username}
                        </div>
                        <div className="text-sm text-gray-500">
                          {user.email}
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center space-x-4">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        user.role === 'ADMIN'
                          ? 'bg-red-100 text-red-800'
                          : user.role === 'TEACHER'
                          ? 'bg-blue-100 text-blue-800'
                          : user.role === 'STUDENT'
                          ? 'bg-green-100 text-green-800'
                          : 'bg-gray-100 text-gray-800'
                      }`}>
                        {user.role}
                      </span>
                      <div className="text-sm text-gray-500">
                        {new Date(user.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
