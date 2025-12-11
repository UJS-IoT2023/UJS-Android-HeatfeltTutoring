import React, { useState, useEffect } from 'react';
import { rewardApi, userApi } from '../api';
import type { RewardDto, UserDto } from '../types';

const RewardManagement: React.FC = () => {
  const [rewards, setRewards] = useState<RewardDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadRewards();
  }, []);

  const loadRewards = async () => {
    try {
      setLoading(true);
      const rewardList = await rewardApi.getRewards();
      setRewards(rewardList);
    } catch (err) {
      console.error('Failed to load rewards:', err);
      setError('加载奖励失败');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full"><div className="text-lg">加载中...</div></div>;
  }

  return (
    <div className="space-y-6">
      {error && <div className="bg-red-50 border border-red-200 rounded-md p-4"><div className="text-red-800">{error}</div></div>}

      <div className="bg-white rounded-lg shadow">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">奖励管理系统 ({rewards.length})</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">标题</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">描述</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">积分</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">发放时间</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {rewards.map((reward) => (
                <tr key={reward.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {reward.title}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900 max-w-xs truncate">
                    {reward.description}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {reward.points}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {new Date(reward.issuedAt).toLocaleDateString('zh-CN')}
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

export default RewardManagement;
