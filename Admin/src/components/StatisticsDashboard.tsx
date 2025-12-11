import React, { useState, useEffect } from 'react';
import { orderApi, userApi, planApi, commentApi } from '../api';
import type { OrderDto, UserDto, PlanDto, CommentDto, Role } from '../types';

const StatisticsDashboard: React.FC = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalOrders: 0,
    totalPlans: 0,
    totalComments: 0,
    usersByRole: {} as Record<Role, number>,
    recentOrders: [] as OrderDto[],
    planCompletion: 0,
    revenueStats: {
      total: 0,
      monthly: 0,
      pending: 0
    }
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      setLoading(true);

      // Load basic stats in parallel
      const [users, orders, plans, allComments] = await Promise.allSettled([
        userApi.getUsers(),
        orderApi.getOrders(),
        planApi.getPlans(),
        loadAllComments()
      ]);

      // Process user stats
      let totalUsers = 0;
      const usersByRole: Record<Role, number> = {
        STUDENT: 0,
        PARENT: 0,
        TEACHER: 0,
        ADMIN: 0
      };

      if (users.status === 'fulfilled') {
        totalUsers = users.value.length;
        users.value.forEach((user: UserDto) => {
          usersByRole[user.role] = (usersByRole[user.role] || 0) + 1;
        });
      }

      // Process order stats
      let totalOrders = 0;
      const revenueStats = { total: 0, monthly: 0, pending: 0 };
      let recentOrders: OrderDto[] = [];

      if (orders.status === 'fulfilled') {
        totalOrders = orders.value.length;
        recentOrders = orders.value.slice(0, 5); // Recent 5 orders

        const now = new Date();
        const thisMonth = new Date(now.getFullYear(), now.getMonth(), 1);

        orders.value.forEach((order: OrderDto) => {
          revenueStats.total += order.amount;

          if (order.status === 'PENDING') {
            revenueStats.pending += order.amount;
          }

          if (order.createdAt && new Date(order.createdAt) >= thisMonth) {
            revenueStats.monthly += order.amount;
          }
        });
      }

      // Process plan stats
      let totalPlans = 0;
      let planCompletion = 0;

      if (plans.status === 'fulfilled') {
        totalPlans = plans.value.length;
        const completedPlans = plans.value.filter((plan: PlanDto) => plan.isCompleted).length;
        planCompletion = totalPlans > 0 ? Math.round((completedPlans / totalPlans) * 100) : 0;
      }

      // Process comment stats
      let totalComments = 0;
      if (allComments.status === 'fulfilled') {
        totalComments = allComments.value.length;
      }

      setStats({
        totalUsers,
        totalOrders,
        totalPlans,
        totalComments,
        usersByRole,
        recentOrders,
        planCompletion,
        revenueStats
      });

    } catch (err) {
      console.error('Failed to load stats:', err);
      setError('加载统计数据失败');
    } finally {
      setLoading(false);
    }
  };

  const loadAllComments = async (): Promise<CommentDto[]> => {
    const userList = await userApi.getUsers();
    const allComments: CommentDto[] = [];
    for (const user of userList.slice(0, 20)) { // Limit to prevent too many requests
      try {
        const userComments = await commentApi.getUserComments(user.id);
        allComments.push(...userComments);
      } catch (err) {
        // Ignore individual user errors
      }
    }
    return allComments;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-lg">加载统计数据中...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-md p-4">
          <div className="text-red-800">{error}</div>
        </div>
      )}

      {/* Key Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center">
                <span className="text-white text-sm font-bold">👥</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">总用户数</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalUsers}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center">
                <span className="text-white text-sm font-bold">💰</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">总订单数</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalOrders}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-purple-500 rounded-full flex items-center justify-center">
                <span className="text-white text-sm font-bold">📚</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">学习计划</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalPlans}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="w-8 h-8 bg-yellow-500 rounded-full flex items-center justify-center">
                <span className="text-white text-sm font-bold">⭐</span>
              </div>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">评价总数</p>
              <p className="text-2xl font-semibold text-gray-900">{stats.totalComments}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Revenue Stats */}
      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">营收统计</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="text-center">
            <p className="text-2xl font-bold text-green-600">¥{stats.revenueStats.total}</p>
            <p className="text-sm text-gray-600">总营收</p>
          </div>
          <div className="text-center">
            <p className="text-2xl font-bold text-blue-600">¥{stats.revenueStats.monthly}</p>
            <p className="text-sm text-gray-600">本月营收</p>
          </div>
          <div className="text-center">
            <p className="text-2xl font-bold text-yellow-600">¥{stats.revenueStats.pending}</p>
            <p className="text-sm text-gray-600">待收款</p>
          </div>
        </div>
      </div>

      {/* User Distribution */}
      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">用户角色分布</h3>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {Object.entries(stats.usersByRole).map(([role, count]) => (
            <div key={role} className="text-center">
              <p className="text-2xl font-semibold text-gray-900">{count}</p>
              <p className="text-sm text-gray-600">{role === 'STUDENT' ? '学生' : role === 'TEACHER' ? '教师' : role === 'PARENT' ? '家长' : '管理员'}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Plan Completion */}
      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-medium text-gray-900 mb-4">学习计划完成率</h3>
        <div className="flex items-center">
          <div className="flex-1">
            <div className="w-full bg-gray-200 rounded-full h-4">
              <div
                className="bg-blue-600 h-4 rounded-full"
                style={{ width: `${stats.planCompletion}%` }}
              ></div>
            </div>
          </div>
          <div className="ml-4">
            <span className="text-lg font-semibold text-blue-600">{stats.planCompletion}%</span>
          </div>
        </div>
      </div>

      {/* Recent Orders */}
      <div className="bg-white rounded-lg shadow">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">最近订单</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">订单ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">金额</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {stats.recentOrders.map((order) => (
                <tr key={order.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    #{order.id}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ¥{order.amount}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                      order.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                      order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-red-100 text-red-800'
                    }`}>
                      {order.status === 'COMPLETED' ? '已完成' :
                       order.status === 'PENDING' ? '待支付' : '已取消'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {order.createdAt ? new Date(order.createdAt).toLocaleDateString('zh-CN') : '-'}
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

export default StatisticsDashboard;
