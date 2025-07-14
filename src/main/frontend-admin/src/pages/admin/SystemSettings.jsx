import React, { useState } from 'react';
import '../../styles/admin/systemSettings/systemSettings.css'; 

function SystemSettings() {
  // 관리자 계정 관리 상태
  const [adminAccounts, setAdminAccounts] = useState([
    { id: 1, username: 'admin' },
  ]);
  const [newAdmin, setNewAdmin] = useState({ username: '', password: '' }); 

  // 이용 약관 및 개인정보 처리방침 상태
  const [termsOfService, setTermsOfService] = useState('여기에 서비스 이용 약관 내용이 들어갑니다.');
  const [privacyPolicy, setPrivacyPolicy] = useState('여기에 개인정보 처리방침 내용이 들어갑니다.');

  // 이미지 설정 상태
  const [defaultProfileImage, setDefaultProfileImage] = useState('/assets/default_profile.png');
  const [defaultPostImage, setDefaultPostImage] = useState('/assets/default_post.png');
  const [imageUploadPath, setImageUploadPath] = useState('/uploads/images/');

  // 외부 서비스 상태 (목업)
  const [serviceStatus, setServiceStatus] = useState({
    redis: '정상',
    fcm: '정상',
    s3: '오류',
  });

  const handleAddAdmin = () => {
    if (newAdmin.username && newAdmin.password) {
      setAdminAccounts([...adminAccounts, { ...newAdmin, id: adminAccounts.length + 1 }]);
      setNewAdmin({ username: '', password: '', role: '운영자' });
    }
  };

  const handleCheckServiceStatus = (service) => {
    // 실제 API 호출 대신 목업 상태 변경
    setServiceStatus(prev => ({
      ...prev,
      [service]: prev[service] === '정상' ? '오류' : '정상' // 토글 예시
    }));
  };

  return (
    <div className="system-settings-page">
      <h2 className="page-title">시스템 설정</h2>

      <section className="settings-section">
        <h3>관리자 계정 및 권한 관리</h3>
        <div className="settings-content">
          <p>관리자 계정 생성, 수정, 삭제 및 각 계정의 권한을 설정합니다.</p>
          <div className="admin-account-management">
            <h4>새 관리자 추가</h4>
            <div className="input-group">
              <input
                type="text"
                placeholder="관리자ID"
                value={newAdmin.username}
                onChange={(e) => setNewAdmin({ ...newAdmin, username: e.target.value })}
              />
              <input
                type="password"
                placeholder="비밀번호"
                value={newAdmin.password}
                onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })}
              />
              <button onClick={handleAddAdmin}>추가</button>
            </div>
            <h4>기존 관리자 목록</h4>
            <ul className="admin-list">
              {adminAccounts.map(account => (
                <li key={account.id}>
                  {account.username} (관리자)
                  {/* 수정/삭제 버튼 등 추가 예정 */}
                </li>
              ))}
            </ul>
          </div>
        </div>
      </section>

      <section className="settings-section">
        <h3>서비스 이용 약관 및 개인정보 처리방침</h3>
        <div className="settings-content">
          <p>사용자에게 제공되는 서비스 이용 약관 및 개인정보 처리방침 내용을 관리합니다.</p>
          <div className="terms-privacy-management">
            <h4>서비스 이용 약관</h4>
            <textarea
              value={termsOfService}
              onChange={(e) => setTermsOfService(e.target.value)}
              rows="10"
            ></textarea>
            <button>저장</button>

            <h4>개인정보 처리방침</h4>
            <textarea
              value={privacyPolicy}
              onChange={(e) => setPrivacyPolicy(e.target.value)}
              rows="10"
            ></textarea>
            <button>저장</button>
          </div>
        </div>
      </section>

      <section className="settings-section">
        <h3>기본 이미지 및 경로 설정</h3>
        <div className="settings-content">
          <p>기본 프로필 이미지, 게시글 기본 이미지, 이미지 저장 경로 등을 설정합니다.</p>
          <div className="image-settings">
            <h4>기본 프로필 이미지</h4>
            <input type="file" />
            <p>현재: {defaultProfileImage}</p>
            <button>업로드</button>

            <h4>기본 게시글 이미지</h4>
            <input type="file" />
            <p>현재: {defaultPostImage}</p>
            <button>업로드</button>

            <h4>이미지 저장 경로</h4>
            <input
              type="text"
              value={imageUploadPath}
              onChange={(e) => setImageUploadPath(e.target.value)}
            />
            <button>저장</button>
          </div>
        </div>
      </section>

      <section className="settings-section">
        <h3>외부 서비스 상태 점검</h3>
        <div className="settings-content">
          <p>Redis, FCM, S3 등 외부 연동 서비스의 현재 상태를 확인합니다.</p>
          <div className="external-service-status">
            <div className="service-item">
              <span>Redis: </span>
              <span className={`status ${serviceStatus.redis === '정상' ? 'status-ok' : 'status-error'}`}>
                {serviceStatus.redis}
              </span>
              <button onClick={() => handleCheckServiceStatus('redis')}>상태 확인</button>
            </div>
            <div className="service-item">
              <span>FCM: </span>
              <span className={`status ${serviceStatus.fcm === '정상' ? 'status-ok' : 'status-error'}`}>
                {serviceStatus.fcm}
              </span>
              <button onClick={() => handleCheckServiceStatus('fcm')}>상태 확인</button>
            </div>
            <div className="service-item">
              <span>S3: </span>
              <span className={`status ${serviceStatus.s3 === '정상' ? 'status-ok' : 'status-error'}`}>
                {serviceStatus.s3}
              </span>
              <button onClick={() => handleCheckServiceStatus('s3')}>상태 확인</button>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

export default SystemSettings;