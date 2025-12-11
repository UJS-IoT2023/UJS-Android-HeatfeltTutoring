import React, { useState, useEffect } from 'react';
import { planApi, userApi } from '../api';
import type { PlanDto, UserDto } from '../types';

const PlanManagement: React.FC = () => {
  const [plans, setPlans] = useState<PlanDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [users, setUsers] = useState<UserDto[]>([]);

  useEffect(() => {
    loadPlans();
    loadUsers();
  }, []);

  const loadPlans = async () => {
    try {
      setLoading(true);
      const planList = await planApi.getPlans();
      setPlans(planList);
    } catch (err) {
      console.error('Failed to load plans:', err);
      setError('加载学习计划失败');
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
          <h3 className="text-lg font-medium text-gray-900">学习计划管理 ({plans.length})</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">学生</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">计划内容</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">截止时间</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {plans.map((plan) => (
                <tr key={plan.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {getUserById(plan.userId)?.username || `用户 ${plan.userId}`}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900 max-w-xs truncate">{plan.content}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {new Date(plan.deadline).toLocaleDateString('zh-CN')}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                      plan.isCompleted ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {plan.isCompleted ? '已完成' : '进行中'}
                    </span>
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

export default PlanManagement;
