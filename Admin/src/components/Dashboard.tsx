import React, { useState } from 'react';
import { useAuth } from '../AuthContext';
import UserManagement from './UserManagement';
import AppointmentManagement from './AppointmentManagement';
import OrderManagement from './OrderManagement';
import PlanManagement from './PlanManagement';
import CommentManagement from './CommentManagement';
import RewardManagement from './RewardManagement';
import ChatMonitoring from './ChatMonitoring';
import StatisticsDashboard from './StatisticsDashboard';

type PanelType = 'users' | 'appointments' | 'orders' | 'plans' | 'comments' | 'rewards' | 'chat' | 'statistics';

const Dashboard: React.FC = () => {
  const [activePanel, setActivePanel] = useState<PanelType>('users');
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  const menuItems = [
    { id: 'users' as PanelType, name: '用户管理', icon: '👥' },
    { id: 'appointments' as PanelType, name: '预约管理', icon: '📅' },
    { id: 'orders' as PanelType, name: '订单管理', icon: '💰' },
    { id: 'plans' as PanelType, name: '学习计划', icon: '📚' },
    { id: 'comments' as PanelType, name: '评价管理', icon: '⭐' },
    { id: 'rewards' as PanelType, name: '奖励系统', icon: '🎁' },
    { id: 'chat' as PanelType, name: '聊天监控', icon: '💬' },
    { id: 'statistics' as PanelType, name: '数据统计', icon: '📊' },
  ];

  const renderPanel = () => {
    switch (activePanel) {
      case 'users':
        return <UserManagement />;
      case 'appointments':
        return <AppointmentManagement />;
      case 'orders':
        return <OrderManagement />;
      case 'plans':
        return <PlanManagement />;
      case 'comments':
        return <CommentManagement />;
      case 'rewards':
        return <RewardManagement />;
      case 'chat':
        return <ChatMonitoring />;
      case 'statistics':
        return <StatisticsDashboard />;
      default:
        return <UserManagement />;
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-lg">
        <div className="flex flex-col h-full">
          {/* Header */}
          <div className="p-6 border-b">
            <h1 className="text-xl font-bold text-gray-900">管理面板</h1>
            <p className="mt-1 text-sm text-gray-600">Heartfelt Tutoring</p>
          </div>

          {/* User Info */}
          <div className="px-6 py-4 border-b bg-gray-50">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                {user?.avatarUrl ? (
                  <img
                    className="h-8 w-8 rounded-full"
                    src={`http://localhost:8080${user.avatarUrl}`}
                    alt={user.username}
                  />
                ) : (
                  <div className="h-8 w-8 rounded-full bg-gray-300 flex items-center justify-center">
                    <span className="text-sm font-medium text-gray-700">
                      {user?.username?.charAt(0).toUpperCase()}
                    </span>
                  </div>
                )}
              </div>
              <div className="ml-3">
                <p className="text-sm font-medium text-gray-900">{user?.username}</p>
                <p className="text-xs text-gray-500">{user?.email}</p>
              </div>
            </div>
          </div>

          {/* Navigation Menu */}
          <nav className="flex-1 px-4 py-6 space-y-2">
            {menuItems.map((item) => (
              <button
                key={item.id}
                onClick={() => setActivePanel(item.id)}
                className={`w-full flex items-center px-4 py-2 text-left text-sm font-medium rounded-md transition-colors ${
                  activePanel === item.id
                    ? 'bg-blue-100 text-blue-700 border-r-2 border-blue-700'
                    : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                <span className="mr-3">{item.icon}</span>
                {item.name}
              </button>
            ))}
          </nav>

          {/* Logout Button */}
          <div className="p-4 border-t">
            <button
              onClick={handleLogout}
              className="w-full flex items-center justify-center px-4 py-2 text-sm font-medium text-red-600 bg-red-50 hover:bg-red-100 rounded-md transition-colors"
            >
              <span className="mr-2">🚪</span>
              登出
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <header className="bg-white shadow-sm">
          <div className="px-8 py-4">
            <h2 className="text-2xl font-bold text-gray-900">
              {menuItems.find(item => item.id === activePanel)?.name}
            </h2>
          </div>
        </header>

        {/* Content Area */}
        <main className="flex-1 overflow-auto p-6">
          {renderPanel()}
        </main>
      </div>
    </div>
  );
};

export default Dashboard;
