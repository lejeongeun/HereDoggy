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
import FavoriteIcon from '@mui/icons-material/Favorite';
import PetsIcon from '@mui/icons-material/Pets';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import VolunteerActivismIcon from '@mui/icons-material/VolunteerActivism';
import GroupIcon from '@mui/icons-material/Group';
import EmojiNatureIcon from '@mui/icons-material/EmojiNature';
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk';
import SmartToyIcon from '@mui/icons-material/SmartToy';
import './Home.css';

// --- 파도 애니메이션 컴포넌트 추가 ---
const AnimatedWave = () => {
  const pathRefs = [useRef(), useRef(), useRef()];

  // 파도 갯수
  const configs = [
    { amp: 25, freq: 2, speed: 0.4, baseY: 70, color: "#E0F7FA", opacity: 1 }, 
    { amp: 20, freq: 2.4, speed: 0.6, baseY: 86, color: "#B2EBF2", opacity: 0.7 }, 
    { amp: 15, freq: 2.1, speed: 0.8, baseY: 95, color: "#80DEEA", opacity: 0.42 }  
  ];

  function makeWavePath({amp, freq, speed, baseY}, phase, width=1440, height=120) {
    let d = `M0,${baseY}`;
    for (let x = 0; x <= width; x += 30) {
      let theta = (x / width) * freq * Math.PI * 2 + phase * speed;
      let y = baseY + amp * Math.sin(theta);
      d += ` L${x},${y}`;
    }
    d += ` L${width},${height} L0,${height} Z`;
    return d;
  }

  useEffect(() => {
    let raf;
    let start = Date.now();
    function animate() {
      const now = Date.now();
      const phase = (now - start) / 1200; // Removed % (Math.PI * 2) for continuous phase
      configs.forEach((cfg, idx) => {
        const path = pathRefs[idx].current;
        if (path) path.setAttribute("d", makeWavePath(cfg, phase + idx * 0.5)); // Adjusted idx offset for visual distinction
      });
      raf = requestAnimationFrame(animate);
    }
    animate();
    return () => cancelAnimationFrame(raf);
  }, []);

  return (
    <div className="hero-wave">
      <svg viewBox="0 0 1440 120" width="100%" height="100%" preserveAspectRatio="none">
        {configs.map((cfg, idx) => (
          <path
            key={idx}
            ref={pathRefs[idx]}
            fill={cfg.color}
            opacity={cfg.opacity}
          />
        ))}
      </svg>
    </div>
  );
};
// --- 여기까지 ---

const getTabMenus = (isAuthenticated) => {
  const baseMenus = [
    { icon: <StorefrontIcon />, label: '스토어', path: '/store' },
    { icon: <HomeIcon />, label: '보호소정보', path: '/shelter' },
    { icon: <ForumIcon />, label: '커뮤니티', path: '/community' },
  ];

  if (isAuthenticated) {
    return [
      ...baseMenus,
      { icon: <PersonIcon />, label: '마이페이지', path: '/mypage' },
      { icon: <AssignmentIcon />, label: '입양신청', path: '/adoption' },
      { icon: <DescriptionIcon />, label: '입양내역', path: '/mypage' },
      { icon: <HelpOutlineIcon />, label: '실종신고/문의', path: '/missing' },
    ];
  } else {
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

const impactStats = [
  { icon: <PetsIcon />, number: 1200, label: '입양 완료' },
  { icon: <VolunteerActivismIcon />, number: 5000, label: '등록 동물' },
  { icon: <LocationOnIcon />, number: 150, label: '협력 보호소' },
  { icon: <GroupIcon />, number: 320, label: '자원봉사자' },
];

function useCountUp(target, duration = 1200) {
  const [count, setCount] = useState(0);
  useEffect(() => {
    let start = 0;
    const end = target;
    if (start === end) return;
    let increment = end / (duration / 16);
    let current = start;
    function step() {
      current += increment;
      if (current < end) {
        setCount(Math.floor(current));
        requestAnimationFrame(step);
      } else {
        setCount(end);
      }
    }
    step();
  }, [target, duration]);
  return count;
}

const coreServices = [
  {
    icon: <DirectionsWalkIcon style={{ fontSize: 64, color: '#3B5BFF' }} />, 
    title: '산책 & 체험 기능',
    desc: '체험/커뮤니티/입양 통합 산책 및 체험 솔루션',
  },
  {
    icon: <SmartToyIcon style={{ fontSize: 64, color: '#3B5BFF' }} />, 
    title: 'AI 기반 맞춤형 유기동물 추천',
    desc: 'MBTI/AI/챗봇을 통한 사용자 맞춤 유기동물 추천',
  },
  {
    icon: <LocationOnIcon style={{ fontSize: 64, color: '#3B5BFF' }} />, 
    title: '사용자 GPS 기반 실시간 제보',
    desc: '유기동물 발견/위급 상황 실시간 제보 및 모니터링',
  },
];

const Home = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated } = useAuth();
  const [heroIdx, setHeroIdx] = useState(0);
  const timeoutRef = useRef(null);
  const [donationAnimated, setDonationAnimated] = useState(false);
  const coreRefs = [useRef(null), useRef(null), useRef(null)];
  const [coreVisible, setCoreVisible] = useState([false, false, false]);

  const tabMenus = getTabMenus(isAuthenticated);
  const infoCards = getInfoCards(isAuthenticated);

  const [impactCounts, setImpactCounts] = useState(impactStats.map(() => 0));
  useEffect(() => {
    let raf;
    const starts = impactStats.map(() => 0);
    const ends = impactStats.map(stat => stat.number);
    const durations = impactStats.map((_, idx) => 1200 + idx * 200);
    const startTime = performance.now();
    function animate(now) {
      const newCounts = starts.map((start, i) => {
        const elapsed = Math.min(now - startTime, durations[i]);
        const progress = Math.min(elapsed / durations[i], 1);
        return Math.floor(start + (ends[i] - start) * progress);
      });
      setImpactCounts(newCounts);
      if (newCounts.some((count, i) => count < ends[i])) {
        raf = requestAnimationFrame(animate);
      }
    }
    raf = requestAnimationFrame(animate);
    return () => raf && cancelAnimationFrame(raf);
  }, []);

  useEffect(() => {
    const handleScroll = () => {
      const donationSection = document.querySelector('.donation-section');
      if (donationSection) {
        const rect = donationSection.getBoundingClientRect();
        const windowHeight = window.innerHeight;
        if (rect.top < windowHeight * 0.7 && !donationAnimated) {
          setDonationAnimated(true);
          donationSection.classList.add('animate');
        }
      }
    };
    window.addEventListener('scroll', handleScroll);
    handleScroll();
    return () => window.removeEventListener('scroll', handleScroll);
  }, [donationAnimated]);

  useEffect(() => {
    timeoutRef.current = setTimeout(() => {
      setHeroIdx((prev) => (prev + 1) % heroImages.length);
    }, 5000);
    return () => clearTimeout(timeoutRef.current);
  }, [heroIdx]);

  useEffect(() => {
    const observers = coreRefs.map((ref, idx) => {
      return new window.IntersectionObserver(
        ([entry]) => {
          if (entry.isIntersecting) {
            setCoreVisible(prev => {
              const updated = [...prev];
              updated[idx] = true;
              return updated;
            });
            ref.current && ref.current.classList.add('visible');
          }
        },
        { threshold: 0.3 }
      );
    });
    coreRefs.forEach((ref, idx) => {
      if (ref.current) observers[idx].observe(ref.current);
    });
    return () => {
      observers.forEach((observer, idx) => {
        if (coreRefs[idx].current) observer.unobserve(coreRefs[idx].current);
      });
    };
  }, []);

  const handleCardClick = (card) => {
    if (card.requiresAuth && !isAuthenticated) {
      alert('로그인이 필요한 서비스입니다. 로그인 후 이용해주세요.');
      navigate('/login');
    } else {
      navigate(card.path);
    }
  };

  const handleActionClick = (path, requiresAuth = false) => {
    if (requiresAuth && !isAuthenticated) {
      alert('로그인이 필요한 서비스입니다. 로그인 후 이용해주세요.');
      navigate('/login');
    } else {
      navigate(path);
    }
  };

  return (
    <div className="home-container">
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
                  자세히 보기
                </button>
              </div>
            )}
          </React.Fragment>
        ))}
        <AnimatedWave />
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

      {/* 여기보개의 핵심 서비스 섹션 (토스 스타일) */}
      <section className="core-services-section">
        <div className="container">
          <h2 className="core-services-title">여기보개의 핵심 서비스</h2>
          <div className="core-services-cards">
            {coreServices.map((svc, idx) => (
              <div
                className={`core-service-card${coreVisible[idx] ? ' visible' : ''}`}
                key={svc.title}
                ref={coreRefs[idx]}
                style={{ transitionDelay: `${idx * 0.15}s` }}
              >
                <div className="core-service-icon">{svc.icon}</div>
                <div className="core-service-title">{svc.title}</div>
                <div className="core-service-desc">{svc.desc}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* 후원 배너 섹션 */}
      <section className="donation-section">
        <div className="container">
          <div className="donation-content">
            <div className="donation-text">
              <h2>함께 만들어가는<br />동물들의 행복한 미래</h2>
              <p>여러분의 후원이 유기동물들에게 새로운 삶의 기회를 줍니다.</p>
              <button className="donation-btn" onClick={() => navigate('/donation')}>
                후원하기
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* 최근 입양 스토리 섹션 */}
      <section className="stories-section">
        <div className="container">
          <h2 className="section-title">최근 입양 스토리</h2>
          <div className="stories-grid">
            <div className="story-item">
              <img src="/images/adoption-story1.jpg" alt="입양 스토리 1" />
              <div className="story-content">
                <h3>루시의 새로운 가족</h3>
                <p>2년간 보호소에서 지내던 루시가 새로운 가족을 만났습니다.</p>
                <span className="story-date">2024.01.15</span>
              </div>
            </div>
            <div className="story-item">
              <img src="/images/adoption-story2.jpg" alt="입양 스토리 2" />
              <div className="story-content">
                <h3>멍멍이들의 행복한 하루</h3>
                <p>입양된 강아지들이 새로운 가족과 함께하는 모습입니다.</p>
                <span className="story-date">2024.01.10</span>
              </div>
            </div>
            <div className="story-item">
              <img src="/images/adoption-story3.jpg" alt="입양 스토리 3" />
              <div className="story-content">
                <h3>보호소에서 가족으로</h3>
                <p>유기동물들이 새로운 가족을 만나 행복해지는 순간들.</p>
                <span className="story-date">2024.01.05</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 보호소 하이라이트 섹션 */}
      <section className="shelter-highlight">
        <div className="container">
          <div className="shelter-content">
            <div className="shelter-text">
              <h2>전국 보호소와<br />함께합니다</h2>
              <p>전국의 다양한 보호소들과 협력하여<br />더 많은 동물들이 새로운 가족을 만날 수 있도록 노력합니다.</p>
              <button className="shelter-btn" onClick={() => navigate('/shelter')}>
                보호소 찾기
              </button>
            </div>
            <div className="shelter-stats">
              <div className="stat-item">
                <span className="stat-number">150+</span>
                <span className="stat-label">협력 보호소</span>
              </div>
              <div className="stat-item">
                <span className="stat-number">1,200+</span>
                <span className="stat-label">입양 완료</span>
              </div>
              <div className="stat-item">
                <span className="stat-number">5,000+</span>
                <span className="stat-label">등록 동물</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
