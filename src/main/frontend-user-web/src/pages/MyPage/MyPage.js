import React, { useState } from 'react';
import './MyPage.css';

const MyPage = () => {
  const [activeTab, setActiveTab] = useState('profile');

  return (
    <div className="mypage">
      <h2>마이페이지</h2>
      
      <div className="profile-section">
        <div className="profile-info">
          <div className="profile-image">
            <img src="/default-avatar.png" alt="프로필" />
          </div>
          <div className="profile-details">
            <h3>사용자님</h3>
            <p>user@example.com</p>
          </div>
        </div>
      </div>

      <div className="tab-container">
        <button 
          className={`tab ${activeTab === 'profile' ? 'active' : ''}`}
          onClick={() => setActiveTab('profile')}
        >
          프로필
        </button>
        <button 
          className={`tab ${activeTab === 'posts' ? 'active' : ''}`}
          onClick={() => setActiveTab('posts')}
        >
          내 게시글
        </button>
        <button 
          className={`tab ${activeTab === 'adoption' ? 'active' : ''}`}
          onClick={() => setActiveTab('adoption')}
        >
          입양내역
        </button>
      </div>

      <div className="content">
        {activeTab === 'profile' && (
          <div className="profile-content">
            <h3>프로필 정보</h3>
            <div className="info-item">
              <label>이름:</label>
              <span>사용자</span>
            </div>
            <div className="info-item">
              <label>이메일:</label>
              <span>user@example.com</span>
            </div>
            <div className="info-item">
              <label>전화번호:</label>
              <span>010-1234-5678</span>
            </div>
          </div>
        )}

        {activeTab === 'posts' && (
          <div className="posts-content">
            <h3>내 게시글</h3>
            <div className="post-list">
              <div className="post-item">
                <h4>반려견과 함께한 즐거운 시간</h4>
                <p>오늘 강아지와 공원에서 놀았는데 정말 즐거웠어요...</p>
                <span className="date">2024.01.15</span>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'adoption' && (
          <div className="adoption-content">
            <h3>입양내역</h3>
            <div className="adoption-list">
              <div className="adoption-item">
                <h4>멍멍이 입양</h4>
                <p>상태: 입양 완료</p>
                <span className="date">2024.01.10</span>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MyPage; 