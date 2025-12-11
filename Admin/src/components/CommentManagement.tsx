import React, { useState, useEffect } from 'react';
import { commentApi, userApi } from '../api';
import type { CommentDto, UserDto } from '../types';

const CommentManagement: React.FC = () => {
  const [comments, setComments] = useState<CommentDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [users, setUsers] = useState<UserDto[]>([]);

  useEffect(() => {
    loadComments();
    loadUsers();
  }, []);

  const loadComments = async () => {
    try {
      setLoading(true);
      // Load comments for all users (simplified)
      const userList = await userApi.getUsers();
      const allComments: CommentDto[] = [];
      for (const user of userList.slice(0, 5)) { // Limit to prevent too many requests
        try {
          const userComments = await commentApi.getUserComments(user.id);
          allComments.push(...userComments);
        } catch (err) {
          // Ignore individual user errors
        }
      }
      setComments(allComments);
    } catch (err) {
      console.error('Failed to load comments:', err);
      setError('加载评价失败');
    } finally {
      setLoading(false);
    }
  };

  const loadUsers = async () => {
    try {
      const userList = await userApi.getUsers();
      setUsers(userList);
    } catch (err) {
      console.error('Failed to load users:', err);
    }
  };

  const getUserById = (userId: number) => {
    return users.find(user => user.id === userId);
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full"><div className="text-lg">加载中...</div></div>;
  }

  return (
    <div className="space-y-6">
      {error && <div className="bg-red-50 border border-red-200 rounded-md p-4"><div className="text-red-800">{error}</div></div>}

      <div className="bg-white rounded-lg shadow">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">评价管理 ({comments.length})</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">评价者</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">被评价者</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">评分</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">评价内容</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">评价时间</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {comments.map((comment) => (
                <tr key={comment.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {getUserById(comment.fromUserId)?.username || `用户 ${comment.fromUserId}`}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {getUserById(comment.toUserId)?.username || `用户 ${comment.toUserId}`}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {comment.rating ? `${comment.rating}/5 ⭐` : '-'}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900 max-w-xs truncate">{comment.content}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {comment.createdAt ? new Date(comment.createdAt).toLocaleDateString('zh-CN') : '-'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default CommentManagement;
