import React from 'react';
import { AuthProvider, useAuth } from './AuthContext';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import './App.css';

const AppContent: React.FC = () => {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-lg">Loading...</div>
      </div>
    );
  }

  return user ? <Dashboard /> : <Login />;
};

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
