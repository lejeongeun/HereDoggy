import React from 'react';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="footer-v2">
      <div className="footer-columns">
        <div className="footer-col">
          <h4>여기보개 안내</h4>
          <ul>
            <li>여기보개 소개</li>
            <li>글로벌 블로그</li>
          </ul>
        </div>
        <div className="footer-col">
          <h4>서비스</h4>
          <ul>
            <li>기업계정 Corporate+</li>
            <li>파트너 셀러 신청하기</li>
            <li>프로모션 코드</li>
            <li>콘텐츠 라이선싱</li>
          </ul>
        </div>
        <div className="footer-col">
          <h4>정책</h4>
          <ul>
            <li>라이선스</li>
            <li>개인정보취급방침</li>
            <li>이용약관</li>
            <li>쿠키 정책</li>
            <li>보안 공개 정책</li>
          </ul>
        </div>
        <div className="footer-col">
          <h4>지원</h4>
          <ul>
            <li>고객지원센터 안내</li>
            <li>도움이 필요하세요? 고객지원센터</li>
            <li>전화: 1544-4225</li>
          </ul>
        </div>
      </div>
      <div className="footer-bottom-v2">
        <div className="footer-company">
          <p>여기보개(주) | 대표: 홍길동 | 사업자등록번호: 123-45-67890<br />
          서울특별시 강남구 테헤란로 123, 2층 (역삼동)<br />
          Copyright © 2025 여기보개. All rights reserved.</p>
        </div>
        <div className="footer-sns">
          <a href="#" aria-label="페이스북">
            <img src="/images/facebook.png" alt="페이스북" className="footer-sns-img" />
          </a>
          <a href="#" aria-label="인스타그램">
            <img src="/images/instagram.png" alt="인스타그램" className="footer-sns-img" />
          </a>
          <a href="#" aria-label="유튜브">
            <img src="/images/youtube.png" alt="유튜브" className="footer-sns-img" />
          </a>
          <a href="#" aria-label="카카오톡">
            <img src="/images/kakao.png" alt="카카오톡" className="footer-sns-img" />
          </a>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 