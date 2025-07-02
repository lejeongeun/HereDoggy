import React, { useState } from 'react';
import './Missing.css';

const Missing = () => {
  const [activeTab, setActiveTab] = useState('list');
  const [showWriteForm, setShowWriteForm] = useState(false);

  const missingPosts = [
    {
      id: 1,
      title: '우리 강아지를 찾습니다',
      dogName: '멍멍이',
      breed: '골든리트리버',
      location: '서울시 강남구',
      date: '2024.01.15',
      description: '어제 오후에 실종되었습니다. 연락주시면 감사하겠습니다.',
      image: '/missing-dog1.jpg',
      contact: '010-1234-5678'
    },
    {
      id: 2,
      title: '댕댕이를 찾습니다',
      dogName: '댕댕이',
      breed: '말티즈',
      location: '서울시 서초구',
      date: '2024.01.14',
      description: '오늘 아침에 실종되었습니다. 목줄에 이름표가 있습니다.',
      image: '/missing-dog2.jpg',
      contact: '010-2345-6789'
    }
  ];

  const [formData, setFormData] = useState({
    title: '',
    dogName: '',
    breed: '',
    location: '',
    description: '',
    contact: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setShowWriteForm(false);
    setFormData({
      title: '',
      dogName: '',
      breed: '',
      location: '',
      description: '',
      contact: ''
    });
  };

  return (
    <div className="missing">
      <h2>실종신고</h2>
      
      <div className="tab-container">
        <button 
          className={`tab ${activeTab === 'list' ? 'active' : ''}`}
          onClick={() => setActiveTab('list')}
        >
          실종신고 목록
        </button>
        <button 
          className={`tab ${activeTab === 'write' ? 'active' : ''}`}
          onClick={() => setActiveTab('write')}
        >
          실종신고 작성
        </button>
      </div>

      {activeTab === 'list' && (
        <div className="missing-list">
          <div className="list-header">
            <h3>실종신고 목록</h3>
            <button 
              className="write-button"
              onClick={() => setShowWriteForm(true)}
            >
              실종신고 작성
            </button>
          </div>
          
          <div className="posts-grid">
            {missingPosts.map((post) => (
              <div key={post.id} className="missing-post">
                <div className="post-image">
                  <img src={post.image} alt={post.dogName} />
                </div>
                <div className="post-content">
                  <h4>{post.title}</h4>
                  <p className="dog-info">
                    <strong>{post.dogName}</strong> ({post.breed})
                  </p>
                  <p className="location">실종 위치: {post.location}</p>
                  <p className="description">{post.description}</p>
                  <p className="contact">연락처: {post.contact}</p>
                  <span className="date">{post.date}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {(activeTab === 'write' || showWriteForm) && (
        <div className="missing-write">
          <h3>실종신고 작성</h3>
          
          <form className="missing-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="title">제목 *</label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                required
                placeholder="예: 우리 강아지를 찾습니다"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="dogName">강아지 이름 *</label>
                <input
                  type="text"
                  id="dogName"
                  name="dogName"
                  value={formData.dogName}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="breed">품종</label>
                <input
                  type="text"
                  id="breed"
                  name="breed"
                  value={formData.breed}
                  onChange={handleChange}
                  placeholder="예: 골든리트리버"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="location">실종 위치 *</label>
              <input
                type="text"
                id="location"
                name="location"
                value={formData.location}
                onChange={handleChange}
                required
                placeholder="예: 서울시 강남구 테헤란로"
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">상세 설명 *</label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows="4"
                required
                placeholder="강아지의 특징, 실종 상황 등을 자세히 적어주세요."
              />
            </div>

            <div className="form-group">
              <label htmlFor="contact">연락처 *</label>
              <input
                type="tel"
                id="contact"
                name="contact"
                value={formData.contact}
                onChange={handleChange}
                required
                placeholder="010-1234-5678"
              />
            </div>

            <div className="form-buttons">
              <button 
                type="button" 
                className="cancel-button"
                onClick={() => {
                  setShowWriteForm(false);
                  setActiveTab('list');
                }}
              >
                취소
              </button>
              <button type="submit" className="submit-button">
                실종신고 등록
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};

export default Missing; 