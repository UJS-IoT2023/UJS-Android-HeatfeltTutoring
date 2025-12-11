export interface LoginRequest {
  identifier: string;
  password: string;
  loginType: 'USERNAME' | 'EMAIL';
}

export interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  message: string;
}

// User related types
export interface UserDto {
  id: number;
  username: string;
  email: string;
  phoneNumber?: string;
  avatarUrl?: string;
  realName?: string;
  gender?: string;
  wechatId?: string;
  qqId?: string;
  address?: string;
  password?: string;
  role: Role;
  teacherProfile?: TeacherProfileDto;
}

export interface User {
  id: number;
  username: string;
  email: string;
  avatarUrl?: string;
  role: 'STUDENT' | 'PARENT' | 'TEACHER' | 'ADMIN';
  createdAt: string;
  updatedAt: string;
}

export interface TeacherProfileDto {
  id?: number;
  subject?: string;
  experience?: number;
  description?: string;
}

export interface SelectUserRequest {
  userId?: number;
  usernameKeyWord?: string;
  role?: Role;
  addressKeyWord?: string;
}

export interface TeacherQueryRequest {
  keyword?: string;
  subject?: Subject;
}

// Appointment types
export interface AppointmentDto {
  id?: number;
  userId: number;
  teacherId: number;
  scheduledTime: string;
  duration: number;
  status: string;
  notes?: string;
}

// Order types
export interface OrderDto {
  id?: number;
  userId: number;
  teacherId: number;
  appointmentId?: number;
  amount: number;
  status: string;
  paymentMethod?: string;
  createdAt?: string;
  completedAt?: string;
}

// Plan types
export interface PlanDto {
  id?: number;
  userId: number;
  content: string;
  deadline: string;
  isCompleted: boolean;
  createdAt?: string;
  updatedAt?: string;
}

// Comment types
export interface CommentDto {
  id?: number;
  fromUserId: number;
  toUserId: number;
  rating?: number;
  content: string;
  createdAt?: string;
}

// Reward types
export interface RewardDto {
  id?: number;
  userId: number;
  title: string;
  description: string;
  points: number;
  issuedAt: string;
}

// Wallet types
export interface WalletDto {
  id?: number;
  userId: number;
  balance: number;
  currency: string;
}

// Chat types
export interface DialogueDto {
  id?: number;
  participantIds: number[];
  title: string;
  createdAt?: string;
}

export interface MessageDto {
  id?: number;
  dialogueId: number;
  senderId: number;
  content: string;
  timestamp: string;
}

// Enum types
export type Role = 'STUDENT' | 'PARENT' | 'TEACHER' | 'ADMIN';
export type Subject = 'MATHEMATICS' | 'PHYSICS' | 'CHEMISTRY' | 'ENGLISH' | 'CHINESE' | 'HISTORY' | 'GEOGRAPHY';

export interface ApiResponse<T> {
  data?: T;
  message?: string;
  error?: string;
}
