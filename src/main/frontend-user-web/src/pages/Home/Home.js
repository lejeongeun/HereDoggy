import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import HomeIcon from '@mui/icons-material/Home';
import PersonIcon from '@mui/icons-material/Person';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import ForumIcon from '@mui/icons-material/Forum';
import AssignmentIcon from '@mui/icons-material/Assignment';
import DescriptionIcon from '@mui/icons-material/Description';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';
import StorefrontIcon from '@mui/icons-material/Storefront';
import './Home.css';

const getTabMenus = (isAuthenticated) => {
  const baseMenus = [
    { icon: <StorefrontIcon />, label: '스토어', path: '/store' },
    { icon: <HomeIcon />, label: '보호소정보', path: '/shelter' },
    { icon: <ForumIcon />, label: '커뮤니티', path: '/community' },
  ];

  if (isAuthenticated) {
    // 로그인된 사용자: 마이페이지, 입양신청, 입양내역, 실종신고/문의 추가
    return [
      ...baseMenus,
      { icon: <PersonIcon />, label: '마이페이지', path: '/mypage' },
      { icon: <AssignmentIcon />, label: '입양신청', path: '/adoption' },
      { icon: <DescriptionIcon />, label: '입양내역', path: '/mypage' },
      { icon: <HelpOutlineIcon />, label: '실종신고/문의', path: '/missing' },
    ];
  } else {
    // 비로그인 사용자: 로그인 탭만 추가
    return [
      ...baseMenus,
      { icon: <VpnKeyIcon />, label: '로그인/로그아웃', path: '/login' },
    ];
  }
};

const getTabActive = (pathname, tabPath) => {
  if (tabPath === '/') return pathname === '/';
  if (tabPath === '/mypage') return pathname === '/mypage' || pathname === '/adoption';
  return pathname.startsWith(tabPath);
};

const getInfoCards = (isAuthenticated) => [
  {
    image: '/adopt-apply.jpg',
    title: '입양신청',
    desc: '반려견 입양을 원하신다면 신청서를 작성해 주세요.',
    btn: '신청하기',
    path: '/adoption',
    requiresAuth: true,
  },
  {
    image: '/adopt-history.jpg',
    title: '입양내역',
    desc: '내가 신청한 입양 내역을 확인할 수 있습니다.',
    btn: '내역보기',
    path: '/mypage',
    requiresAuth: true,
  },
  {
    image: '/shelter-info.jpg',
    title: '보호소정보',
    desc: '입양 가능한 보호소와 강아지 정보를 확인하세요.',
    btn: '보호소보기',
    path: '/shelter',
    requiresAuth: false,
  },
  {
    image: '/dog-info.jpg',
    title: '강아지정보',
    desc: '다양한 강아지들의 정보를 한눈에!',
    btn: '정보보기',
    path: '/shelter',
    requiresAuth: false,
  },
];

const heroImages = [
  '/images/mainImg.png',
  '/images/mainImg2.png',
];

const Home = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated } = useAuth();
  const [heroIdx, setHeroIdx] = useState(0);
  const timeoutRef = useRef(null);

  // 로그인 상태에 따른 탭 메뉴 동적 생성
  const tabMenus = getTabMenus(isAuthenticated);
  const infoCards = getInfoCards(isAuthenticated);

  useEffect(() => {
    timeoutRef.current = setTimeout(() => {
      setHeroIdx((prev) => (prev + 1) % heroImages.length);
    }, 5000);
    return () => clearTimeout(timeoutRef.current);
  }, [heroIdx]);

  const handleCardClick = (card) => {
    if (card.requiresAuth && !isAuthenticated) {
      // 인증이 필요한 기능인데 로그인하지 않은 경우
      alert('로그인이 필요한 서비스입니다. 로그인 후 이용해주세요.');
      navigate('/login');
    } else {
      navigate(card.path);
    }
  };

  return (
    <div className="home-hero-tabs">
      {/* Hero: 강아지 사진 슬라이드 */}
      <div className="hero-img-full">
        {heroImages.map((src, idx) => (
          <React.Fragment key={src}>
            <img
              src={src}
              alt={`강아지와 고양이들${idx + 1}`}
              className={`hero-fade-img${heroIdx === idx ? ' active' : ''}`}
              style={{ zIndex: heroIdx === idx ? 2 : 1 }}
            />
            {idx === 1 && heroIdx === 1 && (
              <div className="hero-overlay-center-left">
                <div className="hero-overlay-text">
                  여기보개는 유기동물과 교감하고<br />입양까지 연결하는<br />
                  <span className="hero-overlay-highlight">참여형 체험 플랫폼</span>입니다
                </div>
                <button className="hero-overlay-btn" onClick={() => navigate('/about')}>
                  더 알아보기
                </button>
              </div>
            )}
          </React.Fragment>
        ))}
        <div className="hero-wave">
          <svg viewBox="0 0 1440 120" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="none">
            <path d="M0,80 Q360,0 720,60 T1440,80 L1440,120 L0,120 Z" fill="#f8f9fa" />
          </svg>
        </div>
        <div className="hero-pagination">
          {heroImages.map((_, idx) => (
            <button
              key={idx}
              className={`hero-dot${heroIdx === idx ? ' active' : ''}`}
              onClick={() => setHeroIdx(idx)}
              aria-label={`이미지 ${idx + 1}번 보기`}
              type="button"
            />
          ))}
        </div>
      </div>
      {/* 탭 메뉴 */}
      <nav className="material-tabs-v2">
        {tabMenus.map(tab => {
          const isActive = getTabActive(location.pathname, tab.path);
          return (
            <button
              key={tab.label}
              className={`material-tab-v2${isActive ? ' active' : ''}`}
              onClick={() => navigate(tab.path)}
              type="button"
            >
              <span className="tab-mat-icon-v2">{tab.icon}</span>
              <span className="tab-mat-label-v2">{tab.label}</span>
              {isActive && <span className="tab-underline-v2" />}
            </button>
          );
        })}
      </nav>
      {/* 하단 카드형 정보 */}
      <section className="info-cards">
        {infoCards.map(card => (
          <div className="info-card" key={card.title}>
            <img src={card.image} alt={card.title} className="card-img" />
            <div className="card-body">
              <h3>{card.title}</h3>
              <p>{card.desc}</p>
              <button 
                className="card-btn" 
                onClick={() => handleCardClick(card)}
              >
                {card.btn}
              </button>
            </div>
          </div>
        ))}
      </section>
    </div>
  );
};

export default Home; 