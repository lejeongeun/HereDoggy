import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();
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
        <button className="header-login-btn" onClick={() => navigate('/login')}>
          로그인
        </button>
      </div>
    </header>
  );
};

export default Header; 