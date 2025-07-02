import React from 'react';
import PetsIcon from '@mui/icons-material/Pets';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import ThumbUpAltIcon from '@mui/icons-material/ThumbUpAlt';
import PsychologyAltIcon from '@mui/icons-material/PsychologyAlt';
import ApartmentIcon from '@mui/icons-material/Apartment';
import CardGiftcardIcon from '@mui/icons-material/CardGiftcard';
import MapIcon from '@mui/icons-material/Map';
import MyLocationIcon from '@mui/icons-material/MyLocation';
import HandshakeIcon from '@mui/icons-material/Handshake';
import DirectionsIcon from '@mui/icons-material/Directions';
import GroupsIcon from '@mui/icons-material/Groups';
import SettingsIcon from '@mui/icons-material/Settings';
import PublicIcon from '@mui/icons-material/Public';
import EmojiNatureIcon from '@mui/icons-material/EmojiNature';
import './About.css';

const impactList = [
  {
    icon: <PetsIcon style={{ fontSize: 48, color: '#3BA55D' }} />,
    value: '15%',
    desc: '초기 입양률 증가',
  },
  {
    icon: <MonetizationOnIcon style={{ fontSize: 48, color: '#3BA55D' }} />,
    value: '30%',
    desc: '사회적 비용 완화',
  },
  {
    icon: <ThumbUpAltIcon style={{ fontSize: 48, color: '#3BA55D' }} />,
    value: '50%',
    desc: 'SNS 긍정 콘텐츠 증가 목표',
  },
];

const donutData = [
  {
    percent: 70,
    label: '전체 유기동물',
    desc: '24년 기준 약 12만 마리\n개 80,393 마리\n고양이 31,525 마리\n기타 : 약 1.3%',
    color: '#3BA55D',
  },
  {
    percent: 27,
    label: '입양률 평균',
    desc: '매년 평균 약 3만 마리\n연 평균 -2.07% 감소',
    color: '#3BA55D',
  },
  {
    percent: 17,
    label: '안락사 비율',
    desc: '매년 평균 2만마리 이상\n연 평균 +0.95% 증가',
    color: '#3BA55D',
  },
  {
    percent: 60,
    label: '사회적 비용',
    desc: '2년 간 사회적 비용 60% 이상 증가\n매년 25% 이상 사회적 비용 상승',
    color: '#3BA55D',
  },
];

function DonutChart({ percent, color }) {
  const radius = 48;
  const stroke = 13;
  const norm = 2 * Math.PI * radius;
  const offset = norm * (1 - percent / 100);
  return (
    <svg width={120} height={120}>
      <circle
        cx={60}
        cy={60}
        r={radius}
        fill="none"
        stroke="#e6e9e6"
        strokeWidth={stroke}
      />
      <circle
        cx={60}
        cy={60}
        r={radius}
        fill="none"
        stroke={color}
        strokeWidth={stroke}
        strokeDasharray={norm}
        strokeDashoffset={offset}
        strokeLinecap="round"
        style={{ transition: 'stroke-dashoffset 0.7s' }}
      />
      <text
        x="60"
        y="68"
        textAnchor="middle"
        fontSize="2.1rem"
        fontWeight="700"
        fill={color}
      >
        {percent}%
      </text>
    </svg>
  );
}

const solutionCards = [
  {
    color: '#3BA55D',
    num: '01',
    title: '공공-민간 연계 플랫폼',
    lines: [
      '산책 자원봉사 및 인력 시스템 도입',
      '지방 지자체와 제휴 산업',
      '굿즈/산책 용품 및 수익 구조 마련',
    ],
  },
  {
    color: '#1976d2',
    num: '02',
    title: '디지털 기반 관리 시스템',
    lines: [
      'AI 기반 보호 등록',
      '위치 연동 구조 대응 시스템',
      '챗봇을 통한 연중무휴 정보 제공',
    ],
  },
  {
    color: '#7c3aed',
    num: '03',
    title: '체험 기반 AI 입양 유도 시스템',
    lines: [
      '산책과 각종 체험을 통해 라포 형성',
      '사용자 성향 별 동물 매칭',
      '관심 동물 유사건 추천',
    ],
  },
];

const featureCards = [
  {
    icon: <PetsIcon style={{ fontSize: 44, color: '#3BA55D' }} />, // 초록
    title: '체험 기반 원스톱 입양 시스템',
    desc: '체험/커뮤니티/입양 통합 솔루션',
    color: '#3BA55D',
  },
  {
    icon: <PsychologyAltIcon style={{ fontSize: 44, color: '#1976d2' }} />, // 파랑
    title: '맞춤형 AI 매칭',
    desc: 'MBTI/AI/챗봇을 통한 사용자 맞춤 매칭',
    color: '#1976d2',
  },
  {
    icon: <ApartmentIcon style={{ fontSize: 44, color: '#7c3aed' }} />, // 보라
    title: '보호소 운영 효율 극대화',
    desc: '지자체 협약/후원 유지/봉사자 매칭',
    color: '#7c3aed',
  },
  {
    icon: <CardGiftcardIcon style={{ fontSize: 44, color: '#eab308' }} />, // 노랑
    title: '지역 상생 및 활성화 제도',
    desc: '보호소-반려사업 협약 및 홍보',
    color: '#eab308',
  },
  {
    icon: <MapIcon style={{ fontSize: 44, color: '#06b6d4' }} />, // 청록
    title: '실시간 산책 예약 및 경로 데이터 제공',
    desc: '산책 예약/경로 지도 제공/모니터링',
    color: '#06b6d4',
  },
  {
    icon: <MyLocationIcon style={{ fontSize: 44, color: '#f43f5e' }} />, // 핑크
    title: '사용자 GPS 기반 실시간 제보',
    desc: '유기동물 발견/위급 상황 실시간 제보',
    color: '#f43f5e',
  },
];

const revenueBlocks = [
  {
    icon: <EmojiNatureIcon style={{ fontSize: 54, color: '#3BA55D' }} />, // 초록 발바닥 대체
    title: <span className="revenue-block-title green">보호소</span>,
    desc: (
      <>
        연관 관리 비용 <b className="green">8억 7,500만원</b><br />
        협약을 통해 마진을 줄여 제품비 15% 인하<br />
        연간 약 <b className="green">1.3억 원</b> 이상 절감 (100곳 기준)
      </>
    ),
  },
  {
    icon: <img src="/images/logo.png" alt="여기보개 로고" style={{ width: 54, height: 54, marginBottom: 6 }} />, // 로고
    title: <span className="revenue-block-title blue">플랫폼</span>,
    desc: (
      <>
        협약체 입점 및 매칭 스토어샵 제공<br />
        <b className="green">10% 수수료 기반</b><br />
        연간 <b className="green">7,400만 원</b> 직접 수익 예상
      </>
    ),
  },
  {
    icon: <HandshakeIcon style={{ fontSize: 54, color: '#1976d2' }} />, // 파랑 악수
    title: <span className="revenue-block-title blue">지역 반려 사업 협약체</span>,
    desc: (
      <>
        보호소 거래 매출 증대 및 <b className="blue">B2C 고객 확장</b><br />
        마케팅 비용 절감 및 안정적 매출 확보<br />
        연간 <b className="green">7.4억 원</b> 이상 추가 매출 추정
      </>
    ),
  },
];

const timelineSteps = [
  {
    year: '2025',
    icon: <DirectionsIcon style={{ fontSize: 48, color: '#1976d2' }} />, // 파랑
    title: 'MVP 론칭 & 수도권 도입',
    desc: (
      <>
        보호소 도입 수: 20곳<br />
        MAU(월간 사용자 수): 1만명<br />
        시장 점유율(SOM 기준): 10%<br />
        <b>3억 원 수익 예상</b>
      </>
    ),
    color: '#1976d2',
  },
  {
    year: '2026',
    icon: <><GroupsIcon style={{ fontSize: 48, color: '#3BA55D', marginRight: -8 }} /><HandshakeIcon style={{ fontSize: 38, color: '#3BA55D', marginLeft: -8, marginBottom: -8 }} /></>, // 초록
    title: '전국 확산 기반 구축',
    desc: (
      <>
        보호소 도입 수: 80곳<br />
        MAU(월간 사용자 수): 5만명<br />
        시장 점유율(SOM 기준): 40%<br />
        <b>12억 원 수익 예상</b>
      </>
    ),
    color: '#3BA55D',
  },
  {
    year: '2027',
    icon: <SettingsIcon style={{ fontSize: 48, color: '#7c3aed' }} />, // 보라
    title: '기능 고도화 + API B2B 확장',
    desc: (
      <>
        보호소 도입 수: 150곳<br />
        MAU(월간 사용자 수): 12만명<br />
        시장 점유율(SOM 기준): 70%<br />
        <b>28억 원 수익 예상</b>
      </>
    ),
    color: '#7c3aed',
  },
  {
    year: '2028',
    icon: <PublicIcon style={{ fontSize: 48, color: '#eab308' }} />, // 노랑
    title: '국내 시장 선점',
    desc: (
      <>
        아카이브 기반 데이터 사업 추진<br />
        보호소 도입 수: 250곳<br />
        MAU(월간 사용자 수): 20만명<br />
        시장 점유율(SOM 기준): 100%<br />
        <b>45억 원 수익 예상</b>
      </>
    ),
    color: '#eab308',
  },
];

const About = () => {
  return (
    <div className="about-page-bg">
      <section className="about-intro">
        <img src="/images/logo.png" alt="여기보개 로고" className="about-logo" />
        <h1 className="about-title">여기보개</h1>
        <p className="about-slogan">
          여기보개는 유기동물과 교감하고 입양까지 연결하는
          <span className="about-highlight"> 참여형 체험 플랫폼</span>입니다.
        </p>
        <p className="about-desc">
          이를 통해 <span className="about-highlight">입양률을 높이고</span>, 보호소의 부담을 줄이며,<br />
          <span className="about-highlight">지역사회와 생명 존중 문화</span>를 동시에 살립니다.
        </p>
      </section>
      <section className="about-impact-cards">
        {impactList.map((item, idx) => (
          <div className="impact-card" key={idx}>
            <div className="impact-icon">{item.icon}</div>
            <div className="impact-value">{item.value}</div>
            <div className="impact-desc">{item.desc}</div>
          </div>
        ))}
      </section>
      <section className="about-bg-section">
        <div className="about-bg-label">유기동물 현황</div>
        <div className="about-bg-maintext">
          <span className="about-bg-strong">연간 <span className="about-highlight">12만 마리</span>의 유기동물이 발견되는 사실, 알고 계셨나요?</span><br />
          하루에 <span className="about-highlight">5마리</span>, 매년 약 <span className="about-highlight">2만 마리</span> 이상의 유기동물은 <span className="about-highlight">안락사</span>되고 있습니다.
        </div>
        <div className="about-bg-donuts">
          {donutData.map((d, i) => (
            <div className="about-bg-donut-card" key={i}>
              <DonutChart percent={d.percent} color={d.color} />
              <div className="about-bg-donut-label">{d.label}</div>
              <div className="about-bg-donut-desc">
                {d.desc.split('\n').map((line, idx) => (
                  <div key={idx}>{line}</div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </section>
      {/* 해결점 제시 섹션 */}
      <section className="about-solution-section">
        <div className="about-solution-title">
          <span className="about-solution-green">행동을 유도</span>하고, 시스템을 <span className="about-solution-blue">혁신</span>하는 <span className="about-solution-gray">구체적인 방안</span>이 필요합니다.
        </div>
        <div className="about-solution-cards">
          {solutionCards.map((card, idx) => (
            <div className="solution-card" key={idx} style={{ borderColor: card.color }}>
              <div className="solution-card-num" style={{ background: card.color }}>{card.num}</div>
              <div className="solution-card-title" style={{ color: card.color }}>{card.title}</div>
              <div className="solution-card-lines">
                {card.lines.map((line, i) => (
                  <React.Fragment key={i}>
                    {i > 0 && <div className="solution-card-divider" />}
                    <div className="solution-card-line">{line}</div>
                  </React.Fragment>
                ))}
              </div>
            </div>
          ))}
        </div>
      </section>
      {/* 기능적 차별점 카드 섹션 */}
      <section className="about-feature-section">
        <div className="about-feature-cards">
          {featureCards.map((card, idx) => (
            <div className="feature-card" key={idx}>
              <div className="feature-card-icon">{card.icon}</div>
              <div className="feature-card-title" style={{ color: card.color }}>{card.title}</div>
              <div className="feature-card-desc">{card.desc}</div>
            </div>
          ))}
        </div>
      </section>
      {/* 수익 모델 분석 블록 섹션 (원+선+중앙정렬) */}
      <section className="about-revenue-section">
        <svg className="about-revenue-connector" width="100%" height="80" viewBox="0 0 1100 80" fill="none" xmlns="http://www.w3.org/2000/svg">
          <line x1="220" y1="40" x2="550" y2="40" stroke="#b7e2c7" strokeWidth="3.5" />
          <line x1="550" y1="40" x2="880" y2="40" stroke="#b7e2c7" strokeWidth="3.5" />
        </svg>
        <div className="about-revenue-blocks">
          {revenueBlocks.map((block, idx) => (
            <div className="revenue-block" key={idx}>
              <div className="revenue-block-icon">{block.icon}</div>
              {block.title}
              <div className="revenue-block-desc">{block.desc}</div>
            </div>
          ))}
        </div>
      </section>
      {/* 시장 점유 목표 & 계획 타임라인 섹션 */}
      <section className="about-timeline-section">
        <svg className="about-timeline-connector" width="100%" height="80" viewBox="0 0 1100 80" fill="none" xmlns="http://www.w3.org/2000/svg">
          <line x1="160" y1="40" x2="366" y2="40" stroke="#b7e2c7" strokeWidth="3.5" />
          <line x1="366" y1="40" x2="733" y2="40" stroke="#b7e2c7" strokeWidth="3.5" />
          <line x1="733" y1="40" x2="940" y2="40" stroke="#b7e2c7" strokeWidth="3.5" />
        </svg>
        <div className="about-timeline-blocks">
          {timelineSteps.map((step, idx) => (
            <div className="timeline-block" key={idx}>
              <div className="timeline-block-year">{step.year}</div>
              <div className="timeline-block-circle" style={{ borderColor: step.color }}>
                {step.icon}
              </div>
              <div className="timeline-block-title" style={{ color: step.color }}>{step.title}</div>
              <div className="timeline-block-desc">{step.desc}</div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};

export default About; 