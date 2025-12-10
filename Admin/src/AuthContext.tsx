import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import type { User } from './types';

interface AuthContextType {
  user: User | null;
  login: (token: string, user: User) => void;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('admin_token');
    const savedUser = localStorage.getItem('admin_user');

    if (token && savedUser) {
      try {
        const parsedUser = JSON.parse(savedUser);
        if (parsedUser.role === 'ADMIN') {
          setUser(parsedUser);
        } else {
          // Not an admin, clear storage
          localStorage.removeItem('admin_token');
          localStorage.removeItem('admin_user');
        }
      } catch (error) {
        console.error('Failed to parse saved user:', error);
        localStorage.removeItem('admin_token');
        localStorage.removeItem('admin_user');
      }
    }
    setIsLoading(false);
  }, []);

  const login = (token: string, user: User) => {
    if (user.role !== 'ADMIN') {
      throw new Error('Access denied. Admin privileges required.');
    }

    localStorage.setItem('admin_token', token);
    localStorage.setItem('admin_user', JSON.stringify(user));
    setUser(user);
  };

  const logout = () => {
    localStorage.removeItem('admin_token');
    localStorage.removeItem('admin_user');
    setUser(null);
  };

  const value: AuthContextType = {
    user,
    login,
    logout,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
