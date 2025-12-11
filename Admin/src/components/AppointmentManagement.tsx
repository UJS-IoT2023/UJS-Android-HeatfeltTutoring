import React, { useState, useEffect } from 'react';
import { appointmentApi, userApi } from '../api';
import type { AppointmentDto, UserDto } from '../types';

const AppointmentManagement: React.FC = () => {
  const [appointments, setAppointments] = useState<AppointmentDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [selectedAppointment, setSelectedAppointment] = useState<AppointmentDto | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isCreating, setIsCreating] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [users, setUsers] = useState<UserDto[]>([]);

  useEffect(() => {
    loadAppointments();
    loadUsers();
  }, []);

  const loadAppointments = async () => {
    try {
      setLoading(true);
      // Since we can't get all appointments directly, let's aggregate them
      const userList = await userApi.getUsers();
      const allAppointments: AppointmentDto[] = [];

      // Get appointments for each user (in a real app, this would be paginated)
      for (const user of userList) {
        try {
          const userAppointments = await appointmentApi.getUserAppointments(user.id);
          allAppointments.push(...userAppointments);
        } catch (err) {
          // Ignore errors for individual users
        }
      }

      setAppointments(allAppointments);
    } catch (err) {
      console.error('Failed to load appointments:', err);
      setError('加载预约失败');
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

  const handleCreateAppointment = () => {
    setSelectedAppointment({
      userId: 0,
      teacherId: 0,
      scheduledTime: '',
      duration: 60,
      status: 'PENDING',
      notes: ''
    });
    setIsCreating(true);
  };

  const handleEditAppointment = (appointment: AppointmentDto) => {
    setSelectedAppointment({ ...appointment });
    setIsEditing(true);
  };

  const handleSaveAppointment = async () => {
    if (!selectedAppointment) return;

    try {
      if (isCreating) {
        await appointmentApi.createAppointment(selectedAppointment);
      } else {
        await appointmentApi.updateAppointment(selectedAppointment.id!, selectedAppointment);
      }

      setIsEditing(false);
      setIsCreating(false);
      setSelectedAppointment(null);
      loadAppointments();
    } catch (err) {
      console.error('Failed to save appointment:', err);
      setError('保存预约失败');
    }
  };

  const handleDeleteAppointment = async (appointmentId: number) => {
    if (!confirm('确定要删除这个预约吗？')) return;

    try {
      await appointmentApi.deleteAppointment(appointmentId);
      loadAppointments();
    } catch (err) {
      console.error('Failed to delete appointment:', err);
      setError('删除预约失败');
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'CONFIRMED': return 'bg-green-100 text-green-800';
      case 'COMPLETED': return 'bg-blue-100 text-blue-800';
      case 'CANCELLED': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const formatStatus = (status: string) => {
    switch (status) {
      case 'PENDING': return '待确认';
      case 'CONFIRMED': return '已确认';
      case 'COMPLETED': return '已完成';
      case 'CANCELLED': return '已取消';
      default: return status;
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-lg">加载中...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold text-gray-900">预约管理</h2>
        <button
          onClick={handleCreateAppointment}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          创建预约
        </button>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-md p-4">
          <div className="text-red-800">{error}</div>
        </div>
      )}

      {/* Appointments Table */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">预约列表 ({appointments.length})</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">学生</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">教师</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">预约时间</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">时长(分钟)</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">备注</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {appointments.map((appointment) => (
                <tr key={appointment.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {getUserById(appointment.userId)?.username || `用户 ${appointment.userId}`}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {getUserById(appointment.teacherId)?.username || `用户 ${appointment.teacherId}`}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {appointment.scheduledTime ? new Date(appointment.scheduledTime).toLocaleString('zh-CN') : '-'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {appointment.duration}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(appointment.status)}`}>
                      {formatStatus(appointment.status)}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {appointment.notes || '-'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                    <button
                      onClick={() => handleEditAppointment(appointment)}
                      className="text-blue-600 hover:text-blue-900"
                    >
                      编辑
                    </button>
                    <button
                      onClick={() => handleDeleteAppointment(appointment.id!)}
                      className="text-red-600 hover:text-red-900"
                    >
                      删除
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Edit/Create Appointment Modal */}
      {(isEditing || isCreating) && selectedAppointment && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50 flex items-center justify-center">
          <div className="relative mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                {isCreating ? '创建预约' : '编辑预约'}
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">学生</label>
                  <select
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedAppointment.userId || ''}
                    onChange={(e) => setSelectedAppointment(prev => ({ ...prev!, userId: parseInt(e.target.value) }))}
                  >
                    <option value="">选择学生</option>
                    {users.filter(u => u.role === 'STUDENT' || u.role === 'PARENT').map(user => (
                      <option key={user.id} value={user.id}>{user.username} ({user.role})</option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">教师</label>
                  <select
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedAppointment.teacherId || ''}
                    onChange={(e) => setSelectedAppointment(prev => ({ ...prev!, teacherId: parseInt(e.target.value) }))}
                  >
                    <option value="">选择教师</option>
                    {users.filter(u => u.role === 'TEACHER').map(user => (
                      <option key={user.id} value={user.id}>{user.username}</option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">预约时间</label>
                  <input
                    type="datetime-local"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedAppointment.scheduledTime || ''}
                    onChange={(e) => setSelectedAppointment(prev => ({ ...prev!, scheduledTime: e.target.value }))}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">时长(分钟)</label>
                  <input
                    type="number"
                    min="15"
                    max="480"
                    step="15"
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedAppointment.duration || 60}
                    onChange={(e) => setSelectedAppointment(prev => ({ ...prev!, duration: parseInt(e.target.value) }))}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">状态</label>
                  <select
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedAppointment.status || 'PENDING'}
                    onChange={(e) => setSelectedAppointment(prev => ({ ...prev!, status: e.target.value }))}
                  >
                    <option value="PENDING">待确认</option>
                    <option value="CONFIRMED">已确认</option>
                    <option value="COMPLETED">已完成</option>
                    <option value="CANCELLED">已取消</option>
                  </select>
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700">备注</label>
                  <textarea
                    rows={3}
                    className="mt-1 w-full border border-gray-300 rounded-md px-3 py-2"
                    value={selectedAppointment.notes || ''}
                    onChange={(e) => setSelectedAppointment(prev => ({ ...prev!, notes: e.target.value }))}
                  />
                </div>
              </div>
            </div>
            <div className="flex justify-end space-x-3 pt-4">
              <button
                onClick={() => {
                  setIsEditing(false);
                  setIsCreating(false);
                  setSelectedAppointment(null);
                }}
                className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
              >
                取消
              </button>
              <button
                onClick={handleSaveAppointment}
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

export default AppointmentManagement;
