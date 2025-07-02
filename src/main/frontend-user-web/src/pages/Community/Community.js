import React, { useState } from 'react';
import './Community.css';

const Community = () => {
  const [activeTab, setActiveTab] = useState('free');

  return (
    <div className="community">
      <h2>커뮤니티</h2>
      
      <div className="tab-container">
        <button 
          className={`tab ${activeTab === 'free' ? 'active' : ''}`}
          onClick={() => setActiveTab('free')}
        >
          자유게시판
        </button>
        <button 
          className={`tab ${activeTab === 'review' ? 'active' : ''}`}
          onClick={() => setActiveTab('review')}
        >
          리뷰게시판
        </button>
      </div>

      <div className="content">
        {activeTab === 'free' ? (
          <div className="free-board">
            <h3>자유게시판</h3>
            <div className="post-list">
              <div className="post-item">
                <h4>반려견과 함께한 즐거운 시간</h4>
                <p>오늘 강아지와 공원에서 놀았는데 정말 즐거웠어요...</p>
                <span className="author">작성자: 강아지맘</span>
                <span className="date">2024.01.15</span>
              </div>
            </div>
          </div>
        ) : (
          <div className="review-board">
            <h3>리뷰게시판</h3>
            <div className="post-list">
              <div className="post-item">
                <h4>보호소 방문 후기</h4>
                <p>지난주에 보호소를 방문했는데 정말 깔끔하고...</p>
                <span className="author">작성자: 입양희망자</span>
                <span className="date">2024.01.14</span>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Community; 