import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../api/resources';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const navigate = useNavigate();
  const [token, setToken] = useState(() => localStorage.getItem('teamflow_token'));
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('teamflow_user');
    return saved ? JSON.parse(saved) : null;
  });

  useEffect(() => {
    const handleUnauthorized = () => logout('/login');
    window.addEventListener('teamflow:unauthorized', handleUnauthorized);
    return () => window.removeEventListener('teamflow:unauthorized', handleUnauthorized);
  }, []);

  const persistSession = (authResponse) => {
    localStorage.setItem('teamflow_token', authResponse.accessToken);
    const authUser = {
      id: authResponse.userId,
      name: authResponse.name,
      email: authResponse.email,
      role: authResponse.role
    };
    localStorage.setItem('teamflow_user', JSON.stringify(authUser));
    setToken(authResponse.accessToken);
    setUser(authUser);
  };

  const login = async (payload) => {
    const response = await authApi.login(payload);
    persistSession(response);
    navigate('/');
  };

  const register = async (payload) => {
    const response = await authApi.register(payload);
    persistSession(response);
    navigate('/');
  };

  const logout = (to = '/login') => {
    localStorage.removeItem('teamflow_token');
    localStorage.removeItem('teamflow_user');
    setToken(null);
    setUser(null);
    navigate(to);
  };

  const value = useMemo(() => ({
    token,
    user,
    isAuthenticated: Boolean(token),
    login,
    register,
    logout
  }), [token, user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}

