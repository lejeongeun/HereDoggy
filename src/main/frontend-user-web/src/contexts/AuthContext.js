import React, { createContext, useContext, useState, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // 사용자 프로필 정보 가져오기
  const fetchUserProfile = async () => {
    try {
      const profileResult = await authService.getProfile();
      if (profileResult.success) {
        setUser(profileResult.data);
        setIsAuthenticated(true);
        return { success: true, data: profileResult.data };
      } else {
        setUser(null);
        setIsAuthenticated(false);
        return { success: false, message: profileResult.message };
      }
    } catch (error) {
      setUser(null);
      setIsAuthenticated(false);
      return { success: false, message: '프로필 조회에 실패했습니다.' };
    }
  };

  useEffect(() => {
    const checkAuth = async () => {
      setLoading(true);
      await fetchUserProfile();
      setLoading(false);
    };
    checkAuth();
  }, []);

  // 로그인
  const login = async (email, password) => {
    const result = await authService.login(email, password);
    if (result.success) {
      // 로그인 성공 시 즉시 프로필 재조회
      const profileResult = await fetchUserProfile();
      if (!profileResult.success) {
        setUser(null);
        setIsAuthenticated(false);
      }
    }
    return result;
  };

  // 로그아웃
  const logout = async () => {
    await authService.logout();
    setIsAuthenticated(false);
    setUser(null);
  };

  // 사용자 정보 새로고침
  const refreshUser = async () => {
    return await fetchUserProfile();
  };

  const value = {
    isAuthenticated,
    user,
    loading,
    login,
    logout,
    refreshUser
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}; 