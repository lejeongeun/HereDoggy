import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { Button, Menu, MenuItem, Avatar, Typography } from '@mui/material';
import { useState } from 'react';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();
  const { isAuthenticated, user, logout } = useAuth();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = async () => {
    await logout();
    handleMenuClose();
    navigate('/');
  };

  const handleProfileClick = () => {
    handleMenuClose();
    navigate('/mypage');
  };

  return (
    <header className="header">
      <div className="header-content">
        <div className="logo" onClick={() => navigate('/')} style={{cursor: 'pointer'}}>
          <img src="/images/logo.png" alt="여기보개 로고" className="logo-img" />
        </div>
        <div className="header-chatbot-search">
          <input
            type="text"
            className="chatbot-input"
            placeholder="무엇이든 물어보세요…"
          />
        </div>
        {isAuthenticated ? (
          <div className="header-user-section">
            <Button
              onClick={handleMenuClick}
              className="header-user-btn"
              startIcon={
                <Avatar 
                  sx={{ width: 32, height: 32, fontSize: '14px' }}
                  alt={user?.nickname || '사용자'}
                >
                  {user?.nickname?.charAt(0) || 'U'}
                </Avatar>
              }
            >
              <Typography variant="body2" sx={{ ml: 1 }}>
                {user?.nickname || '사용자'}
              </Typography>
            </Button>
            <Menu
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={handleMenuClose}
              className="header-user-menu"
            >
              <MenuItem onClick={handleProfileClick}>마이페이지</MenuItem>
              <MenuItem onClick={handleLogout}>로그아웃</MenuItem>
            </Menu>
          </div>
        ) : (
          <Button 
            className="header-login-btn" 
            onClick={() => navigate('/login')}
            variant="contained"
          >
            로그인
          </Button>
        )}
      </div>
    </header>
  );
};

export default Header; 