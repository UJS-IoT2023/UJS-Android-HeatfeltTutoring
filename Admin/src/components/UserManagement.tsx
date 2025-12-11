import React, { useState, useEffect } from 'react';
import { userApi } from '../api';
import type { UserDto, SelectUserRequest, Role, TeacherQueryRequest, Subject } from '../types';

const UserManagement: React.FC = () => {
  const [users, setUsers] = useState<UserDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [searchForm, setSearchForm] = useState<SelectUserRequest>({});
  const [selectedUser, setSelectedUser] = useState<UserDto | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [teacherQuery, setTeacherQuery] = useState<TeacherQueryRequest>({});
  const [teachers, setTeachers] = useState<UserDto[]>([]);

  useEffect(() => {
    loadUsers();
    loadTeachers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const result = Object.keys(searchForm).some(key => searchForm[key as keyof SelectUserRequest])
        ? await userApi.searchUsers(searchForm)
        : await userApi.getUsers();
      setUsers(result);
    } catch (err) {
      console.error('Failed to load users:', err);
      setError('加载用户失败');
    } finally {
      setLoading(false);
    }
  };

  const loadTeachers = async () => {
    try {
      const result = Object.keys(teacherQuery).some(key => teacherQuery[key as keyof TeacherQueryRequest])
        ? await userApi.searchTeachers(teacherQuery)
        : await userApi.getTeachers();
      setTeachers(result);
    } catch (err) {
      console.error('Failed to load teachers:', err);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    loadUsers();
    loadTeachers();
  };

  const handleEdituser = (user: UserDto) => {
    setSelectedUser({ ...user });
    setIsEditing(true);
  };

  const handleSaveUser = async () => {
    if (!selectedUser) return;

    try {
      await userApi.updateUser(selectedUser.id!, selectedUser);
      setIsEditing(false);
      setSelectedUser(null);
      loadUsers();
      loadTeachers();
    } catch (err) {
      console.error('Failed to update user:', err);
      setError('更新用户失败');
    }
  };

  const getRoleColor = (role: Role) => {
    switch (role) {
      case 'ADMIN': return 'bg-red-100 text-red-800';
      case 'TEACHER': return 'bg-blue-100 text-blue-800';
      case 'STUDENT': return 'bg-green-100 text-green-800';
      case 'PARENT': return 'bg-purple-100 text-purple-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  if (loading && users.length === 0) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-lg">加载中...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Search Form */}
      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">搜索条件</h3>
        <form onSubmit={handleSearch} className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">用户名关键词</label>
            <input
              type="text"
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
              value={searchForm.usernameKeyWord || ''}
              onChange={(e) => setSearchForm(prev => ({ ...prev, usernameKeyWord: e.target.value }))}
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">角色</label>
            <select
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
              value={searchForm.role || ''}
              onChange={(e) => setSearchForm(prev => ({ ...prev, role: e.target.value as Role }))}
            >
              <option value="">全部</option>
              <option value="STUDENT">学生</option>
              <option value="TEACHER">教师</option>
              <option value="PARENT">家长</option>
              <option value="ADMIN">管理员</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">地址关键词</label>
            <input
              type="text"
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
              value={searchForm.addressKeyWord || ''}
              onChange={(e) => setSearchForm(prev => ({ ...prev, addressKeyWord: e.target.value }))}
            />
          </div>
          <div className="flex items-end">
            <button
              type="submit"
              className="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
            >
              搜索
            </button>
          </div>
        </form>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-md p-4">
          <div className="text-red-800">{error}</div>
        </div>
      )}

      {/* User List */}
      <div className="bg-white rounded-lg shadow">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">用户列表 ({users.length})</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">头像</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户名</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">邮箱</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">角色</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">地址</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {users.map((user) => (
                <tr key={user.id}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {user.avatarUrl ? (
                      <img className="h-10 w-10 rounded-full" src={`http://localhost:8080${user.avatarUrl}`} alt={user.username} />
                    ) : (
                      <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                        <span className="text-sm font-medium text-gray-700">
                          {user.username?.charAt(0).toUpperCase()}
                        </span>
                      </div>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {user.username}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {user.email}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getRoleColor(user.role as Role)}`}>
                      {user.role}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {user.address || '-'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button
                      onClick={() => handleEdituser(user)}
                      className="text-blue-600 hover:text-blue-900 mr-3"
                    >
                      编辑
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Teachers Section */}
      <div className="bg-white rounded-lg shadow">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">教师列表 ({teachers.length})</h3>
        </div>
        <div className="p-6">
          <form onSubmit={handleSearch} className="mb-4 grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">学科关键词</label>
              <input
                type="text"
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
                value={teacherQuery.keyword || ''}
                onChange={(e) => setTeacherQuery(prev => ({ ...prev, keyword: e.target.value }))}
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">学科</label>
              <select
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
                value={teacherQuery.subject || ''}
                onChange={(e) => setTeacherQuery(prev => ({ ...prev, subject: e.target.value as Subject }))}
              >
                <option value="">全部</option>
                <option value="MATHEMATICS">数学</option>
                <option value="PHYSICS">物理</option>
                <option value="CHEMISTRY">化学</option>
                <option value="ENGLISH">英语</option>
                <option value="CHINESE">语文</option>
                <option value="HISTORY">历史</option>
                <option value="GEOGRAPHY">地理</option>
              </select>
            </div>
            <div className="flex items-end">
              <button
                type="submit"
                className="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                搜索教师
              </button>
            </div>
          </form>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {teachers.slice(0, 9).map((teacher) => (
              <div key={teacher.id} className="border border-gray-200 rounded-lg p-4">
                <div className="flex items-center mb-2">
                  {teacher.avatarUrl ? (
                    <img className="h-8 w-8 rounded-full mr-3" src={`http://localhost:8080${teacher.avatarUrl}`} alt={teacher.username} />
                  ) : (
                    <div className="h-8 w-8 rounded-full bg-gray-300 flex items-center justify-center mr-3">
                      <span className="text-xs font-medium text-gray-700">
                        {teacher.username?.charAt(0).toUpperCase()}
                      </span>
                    </div>
                  )}
                  <div>
                    <h4 className="text-sm font-medium text-gray-900">{teacher.username}</h4>
                    <p className="text-xs text-gray-500">{teacher.teacherProfile?.subject || '-'}</p>
                  </div>
                </div>
                <p className="text-xs text-gray-600">{teacher.teacherProfile?.description || '-'}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Edit User Modal */}
      {isEditing && selectedUser && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50 flex items-center justify-center">
          <div className="relative mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">编辑用户信息</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">用户名</label>
                  <input
                    type="text"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedUser.username || ''}
                    onChange={(e) => setSelectedUser(prev => ({ ...prev!, username: e.target.value }))}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">邮箱</label>
                  <input
                    type="email"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedUser.email || ''}
                    onChange={(e) => setSelectedUser(prev => ({ ...prev!, email: e.target.value }))}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">真实姓名</label>
                  <input
                    type="text"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedUser.realName || ''}
                    onChange={(e) => setSelectedUser(prev => ({ ...prev!, realName: e.target.value }))}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">手机号码</label>
                  <input
                    type="text"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedUser.phoneNumber || ''}
                    onChange={(e) => setSelectedUser(prev => ({ ...prev!, phoneNumber: e.target.value }))}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">性别</label>
                  <select
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedUser.gender || ''}
                    onChange={(e) => setSelectedUser(prev => ({ ...prev!, gender: e.target.value }))}
                  >
                    <option value="">请选择</option>
                    <option value="MALE">男</option>
                    <option value="FEMALE">女</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">地址</label>
                  <input
                    type="text"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedUser.address || ''}
                    onChange={(e) => setSelectedUser(prev => ({ ...prev!, address: e.target.value }))}
                  />
                </div>
              </div>
            </div>
            <div className="flex justify-end space-x-3 pt-4">
              <button
                onClick={() => setIsEditing(false)}
                className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
              >
                取消
              </button>
              <button
                onClick={handleSaveUser}
                className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
              >
                保存
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserManagement;
