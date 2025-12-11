import axios from 'axios';
import type {
  LoginRequest,
  LoginResponse,
  User,
  UserDto,
  SelectUserRequest,
  TeacherQueryRequest,
  AppointmentDto,
  OrderDto,
  PlanDto,
  CommentDto,
  RewardDto,
  WalletDto,
  DialogueDto,
  MessageDto
} from './types';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth APIs
export const authApi = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post('/auth/login', data);
    return response.data;
  },

  sendVerificationCode: async (email: string): Promise<{ message: string }> => {
    const response = await api.post('/auth/send-verification-code', { email });
    return response.data;
  },

  verifyEmail: async (email: string, code: string): Promise<any> => {
    const response = await api.post('/auth/verify-email', { email, code });
    return response.data;
  },

  register: async (data: any): Promise<any> => {
    const response = await api.post('/auth/register', data);
    return response.data;
  },

  verifyToken: async (): Promise<any> => {
    const response = await api.post('/auth/verify');
    return response.data;
  },

  getUser: async (userId: number): Promise<User> => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
  },
};

// User management APIs
export const userApi = {
  getUsers: async (): Promise<UserDto[]> => {
    const response = await api.get('/users');
    return response.data;
  },

  searchUsers: async (data: SelectUserRequest): Promise<UserDto[]> => {
    const response = await api.post('/users', data);
    return response.data;
  },

  getUser: async (userId: number): Promise<UserDto> => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
  },

  updateUser: async (userId: number, data: UserDto): Promise<UserDto> => {
    const response = await api.put(`/users/${userId}`, data);
    return response.data;
  },

  getTeachers: async (): Promise<UserDto[]> => {
    const response = await api.get('/users/teachers');
    return response.data;
  },

  searchTeachers: async (data: TeacherQueryRequest): Promise<UserDto[]> => {
    const response = await api.post('/users/teachers', data);
    return response.data;
  },

  uploadAvatar: async (userId: number, file: File): Promise<{ success: boolean; message: string; url: string }> => {
    const formData = new FormData();
    formData.append('avatar', file);
    const response = await api.post(`/users/upload-avatar/${userId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },
};

// Appointment APIs
export const appointmentApi = {
  getUserAppointments: async (userId: number): Promise<AppointmentDto[]> => {
    const response = await api.get(`/appointments/user/${userId}`);
    return response.data;
  },

  createAppointment: async (data: AppointmentDto): Promise<AppointmentDto> => {
    const response = await api.post('/appointments', data);
    return response.data;
  },

  updateAppointment: async (appointmentId: number, data: AppointmentDto): Promise<AppointmentDto> => {
    const response = await api.put(`/appointments/${appointmentId}`, data);
    return response.data;
  },

  deleteAppointment: async (appointmentId: number): Promise<void> => {
    await api.delete(`/appointments/${appointmentId}`);
  },
};

// Order APIs
export const orderApi = {
  getOrders: async (): Promise<OrderDto[]> => {
    const response = await api.get('/orders');
    return response.data;
  },

  getUserOrders: async (userId: number): Promise<OrderDto[]> => {
    const response = await api.get(`/orders/user/${userId}`);
    return response.data;
  },

  getOrdersByStatus: async (status: string): Promise<OrderDto[]> => {
    const response = await api.get(`/orders/state/${status}`);
    return response.data;
  },

  createOrder: async (data: OrderDto): Promise<OrderDto> => {
    const response = await api.post('/orders', data);
    return response.data;
  },

  updateOrder: async (orderId: number, data: OrderDto): Promise<OrderDto> => {
    const response = await api.put(`/orders/${orderId}`, data);
    return response.data;
  },

  deleteOrder: async (orderId: number): Promise<void> => {
    await api.delete(`/orders/${orderId}`);
  },
};

// Study plans APIs
export const planApi = {
  getPlans: async (): Promise<PlanDto[]> => {
    const response = await api.get('/plans');
    return response.data;
  },

  getUserPlans: async (userId: number): Promise<PlanDto[]> => {
    const response = await api.get(`/plans/user/${userId}`);
    return response.data;
  },

  getPlansByStatus: async (isCompleted: boolean): Promise<PlanDto[]> => {
    const response = await api.get(`/plans/status/${isCompleted}`);
    return response.data;
  },

  createPlan: async (data: PlanDto): Promise<PlanDto> => {
    const response = await api.post('/plans', data);
    return response.data;
  },

  togglePlanStatus: async (planId: number): Promise<PlanDto> => {
    const response = await api.put(`/plans/toggle/${planId}`);
    return response.data;
  },

  updatePlan: async (planId: number, data: PlanDto): Promise<PlanDto> => {
    const response = await api.put(`/plans/${planId}`, data);
    return response.data;
  },

  deletePlan: async (planId: number): Promise<void> => {
    await api.delete(`/plans/${planId}`);
  },
};

// Comments APIs
export const commentApi = {
  getUserComments: async (userId: number): Promise<CommentDto[]> => {
    const response = await api.get(`/comments/user/${userId}`);
    return response.data;
  },

  createComment: async (data: CommentDto): Promise<CommentDto> => {
    const response = await api.post('/comments', data);
    return response.data;
  },
};

// Rewards APIs
export const rewardApi = {
  getRewards: async (): Promise<RewardDto[]> => {
    const response = await api.get('/rewards');
    return response.data;
  },

  getUserRewards: async (userId: number): Promise<RewardDto[]> => {
    const response = await api.get(`/rewards/user/${userId}`);
    return response.data;
  },

  createReward: async (data: RewardDto): Promise<RewardDto> => {
    const response = await api.post('/rewards', data);
    return response.data;
  },

  updateReward: async (rewardId: number, data: RewardDto): Promise<RewardDto> => {
    const response = await api.put(`/rewards/${rewardId}`, data);
    return response.data;
  },

  deleteReward: async (rewardId: number): Promise<void> => {
    await api.delete(`/rewards/${rewardId}`);
  },
};

// Wallet APIs
export const walletApi = {
  getUserWallet: async (userId: number): Promise<WalletDto> => {
    const response = await api.get(`/wallets/user/${userId}`);
    return response.data;
  },

  updateWallet: async (walletId: number, data: WalletDto): Promise<WalletDto> => {
    const response = await api.put(`/wallets/${walletId}`, data);
    return response.data;
  },

  deleteWallet: async (walletId: number): Promise<void> => {
    await api.delete(`/wallets/${walletId}`);
  },
};

// Chat APIs
export const chatApi = {
  getUserDialogues: async (userId: number): Promise<DialogueDto[]> => {
    const response = await api.get(`/chat/dialogues/${userId}`);
    return response.data;
  },

  createDialogue: async (data: DialogueDto): Promise<DialogueDto> => {
    const response = await api.post('/chat/dialogue', data);
    return response.data;
  },

  getDialogueMessages: async (dialogueId: number): Promise<MessageDto[]> => {
    const response = await api.get(`/chat/dialogue/${dialogueId}/messages`);
    return response.data;
  },
};

// AI APIs
export const aiApi = {
  generateContent: async (data: { message: string; conversationId?: string }): Promise<any> => {
    const response = await api.post('/ai/generate', data);
    return response.data;
  },

  chatStream: async (data: any): Promise<any> => {
    const response = await api.post('/ai/chat', data);
    return response.data;
  },
};

export default api;
