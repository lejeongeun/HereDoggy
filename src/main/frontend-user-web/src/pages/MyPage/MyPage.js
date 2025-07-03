import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import memberService from '../../services/memberService';
import './MyPage.css';

const MyPage = () => {
  const { isAuthenticated, user, logout, refreshUser } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('profile');
  const [myPosts, setMyPosts] = useState(null);
  const [myAdoptions, setMyAdoptions] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [imageError, setImageError] = useState(false);

  // 로그인하지 않은 경우 로그인 페이지로 이동
  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
    }
  }, [isAuthenticated, navigate]);

  // 탭 변경 시에만 데이터 로드 (user 변경 시에는 로드하지 않음)
  useEffect(() => {
    if (isAuthenticated && user) {
      loadTabData();
    }
  }, [activeTab]); // user 의존성 제거

  const loadTabData = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      if (activeTab === 'posts') {
        const result = await memberService.getMyPosts();
        if (result.success) {
          setMyPosts(result.data);
        } else {
          setError(result.message);
        }
      } else if (activeTab === 'adoption') {
        const result = await memberService.getMyAdoptions();
        if (result.success) {
          setMyAdoptions(result.data);
        } else {
          setError(result.message);
        }
      }
    } catch (err) {
      setError('데이터를 불러오는 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  }, [activeTab]);

  // 로그인하지 않은 경우 로딩 화면 표시
  if (!isAuthenticated) {
    return (
      <div className="mypage">
        <div className="loading">로딩 중...</div>
      </div>
    );
  }

  // 사용자 정보가 없는 경우
  if (!user) {
    return (
      <div className="mypage">
        <div className="error">사용자 정보를 불러올 수 없습니다.</div>
      </div>
    );
  }

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  const handleRefreshUser = async () => {
    const result = await refreshUser();
    if (!result.success) {
      alert('사용자 정보 새로고침에 실패했습니다.');
    }
  };

  const handleImageError = (e) => {
    setImageError(true);
    e.target.src = "/default-avatar.png";
  };

  // 주소 파싱 함수
  const parseAddress = (address) => {
    if (!address) return '주소 정보 없음';
    return address;
  };

  // 생년월일 포맷팅
  const formatBirth = (birth) => {
    if (!birth) return '생년월일 정보 없음';
    return birth;
  };

  // 날짜 포맷팅
  const formatDate = (dateString) => {
    if (!dateString) return '날짜 정보 없음';
    return new Date(dateString).toLocaleDateString('ko-KR');
  };

  // 게시글 타입별 이름
  const getPostTypeName = (type) => {
    switch (type) {
      case 'FREE': return '자유게시판';
      case 'REVIEW': return '후기게시판';
      case 'MISSING': return '실종신고';
      default: return '게시글';
    }
  };

  // 입양 상태별 이름
  const getAdoptionStatusName = (status) => {
    switch (status) {
      case 'PENDING': return '대기중';
      case 'APPROVED': return '승인됨';
      case 'REJECTED': return '거절됨';
      case 'COMPLETED': return '입양완료';
      default: return status;
    }
  };

  // 프로필 이미지 URL 결정
  const getProfileImageUrl = () => {
    if (imageError || !user.profileImageUrl) {
      return "/default-avatar.png";
    }
    return user.profileImageUrl;
  };

  return (
    <div className="mypage">
      <h2>마이페이지</h2>
      
      <div className="profile-section">
        <div className="profile-info">
          <div className="profile-image">
            <img 
              src={user.profileImageUrl ? user.profileImageUrl : "/default-avatar.png"}
              alt="프로필"
              onError={e => {
                // 이미 기본 이미지면 더 이상 변경하지 않음
                if (!e.target.src.endsWith('/default-avatar.png')) {
                  e.target.src = "/default-avatar.png";
                }
              }}
            />
          </div>
          <div className="profile-details">
            <h3>{user.nickname || user.name || '사용자'}님</h3>
            <p>{user.email}</p>
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
              <span>{user.name || '정보 없음'}</span>
            </div>
            <div className="info-item">
              <label>닉네임:</label>
              <span>{user.nickname || '정보 없음'}</span>
            </div>
            <div className="info-item">
              <label>이메일:</label>
              <span>{user.email}</span>
            </div>
            <div className="info-item">
              <label>전화번호:</label>
              <span>{user.phone || '정보 없음'}</span>
            </div>
            <div className="info-item">
              <label>생년월일:</label>
              <span>{formatBirth(user.birth)}</span>
            </div>
            <div className="info-item">
              <label>주소:</label>
              <span>{parseAddress(user.address)}</span>
            </div>
            <div className="info-item">
              <label>총 산책 거리:</label>
              <span>{user.totalWalkDistance ? `${user.totalWalkDistance.toFixed(2)}km` : '0km'}</span>
            </div>
            <div className="info-item">
              <label>총 산책 시간:</label>
              <span>
                {user.totalWalkDuration 
                  ? `${Math.floor(user.totalWalkDuration / 60)}분 ${user.totalWalkDuration % 60}초`
                  : '0분 0초'
                }
              </span>
            </div>
            <div className="info-item">
              <label>가입일:</label>
              <span>
                {user.createdAt 
                  ? new Date(user.createdAt).toLocaleDateString('ko-KR')
                  : '정보 없음'
                }
              </span>
            </div>
            <div className="profile-actions">
              <button className="edit-btn" onClick={() => navigate('/profile/edit')}>
                프로필 수정
              </button>
              <button className="refresh-btn" onClick={handleRefreshUser}>
                새로고침
              </button>
              <button className="logout-btn" onClick={handleLogout}>
                로그아웃
              </button>
            </div>
          </div>
        )}

        {activeTab === 'posts' && (
          <div className="posts-content">
            <h3>내 게시글</h3>
            {loading ? (
              <div className="loading">게시글을 불러오는 중...</div>
            ) : error ? (
              <div className="error">{error}</div>
            ) : myPosts ? (
              <div className="post-list">
                {myPosts.freePosts && myPosts.freePosts.length > 0 && (
                  <div className="post-section">
                    <h4>자유게시판</h4>
                    {myPosts.freePosts.map((post, index) => (
                      <div key={`free-${index}`} className="post-item">
                        <h5>{post.title}</h5>
                        <p>{post.content}</p>
                        <span className="date">{formatDate(post.createdAt)}</span>
                      </div>
                    ))}
                  </div>
                )}
                {myPosts.reviewPosts && myPosts.reviewPosts.length > 0 && (
                  <div className="post-section">
                    <h4>후기게시판</h4>
                    {myPosts.reviewPosts.map((post, index) => (
                      <div key={`review-${index}`} className="post-item">
                        <h5>{post.title}</h5>
                        <p>{post.content}</p>
                        <span className="date">{formatDate(post.createdAt)}</span>
                      </div>
                    ))}
                  </div>
                )}
                {myPosts.missingPosts && myPosts.missingPosts.length > 0 && (
                  <div className="post-section">
                    <h4>실종신고</h4>
                    {myPosts.missingPosts.map((post, index) => (
                      <div key={`missing-${index}`} className="post-item">
                        <h5>{post.title}</h5>
                        <p>{post.content}</p>
                        <span className="date">{formatDate(post.createdAt)}</span>
                      </div>
                    ))}
                  </div>
                )}
                {(!myPosts.freePosts || myPosts.freePosts.length === 0) &&
                 (!myPosts.reviewPosts || myPosts.reviewPosts.length === 0) &&
                 (!myPosts.missingPosts || myPosts.missingPosts.length === 0) && (
                  <div className="no-data">작성한 게시글이 없습니다.</div>
                )}
              </div>
            ) : (
              <div className="no-data">게시글 정보를 불러올 수 없습니다.</div>
            )}
          </div>
        )}

        {activeTab === 'adoption' && (
          <div className="adoption-content">
            <h3>입양내역</h3>
            {loading ? (
              <div className="loading">입양내역을 불러오는 중...</div>
            ) : error ? (
              <div className="error">{error}</div>
            ) : myAdoptions ? (
              <div className="adoption-list">
                {myAdoptions.length > 0 ? (
                  myAdoptions.map((adoption, index) => (
                    <div key={index} className="adoption-item">
                      <h4>{adoption.dogName || '강아지'} 입양</h4>
                      <p>상태: {getAdoptionStatusName(adoption.status)}</p>
                      <p>보호소: {adoption.shelterName || '정보 없음'}</p>
                      <span className="date">{formatDate(adoption.createdAt)}</span>
                    </div>
                  ))
                ) : (
                  <div className="no-data">입양 신청 내역이 없습니다.</div>
                )}
              </div>
            ) : (
              <div className="no-data">입양내역 정보를 불러올 수 없습니다.</div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default MyPage; 