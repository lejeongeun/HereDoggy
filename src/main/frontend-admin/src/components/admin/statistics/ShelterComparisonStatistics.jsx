import React from 'react';
import ProgressBar from './common/ProgressBar';

function ShelterComparisonStatistics() {
  // 보호소별 통계 비교 mock 데이터
  const shelterStats = [
    { name: '행복보호소', walks: 120, adoptions: 32, reviews: 14, rating: 4.7 },
    { name: '희망쉼터', walks: 98, adoptions: 28, reviews: 11, rating: 4.5 },
    { name: '사랑의집', walks: 87, adoptions: 21, reviews: 9, rating: 4.2 },
    { name: '푸른쉼터', walks: 75, adoptions: 18, reviews: 7, rating: 4.0 },
    { name: '햇살보호소', walks: 62, adoptions: 14, reviews: 5, rating: 3.9 },
  ];
  return (
    <section className="shelter-comparison-section">
      <div style={{ fontWeight: 600, marginBottom: 10 }}>보호소별 활동 지표 비교</div>
      <table className="shelter-comparison-table">
        <thead>
          <tr>
            <th>보호소</th><th>산책</th><th>입양</th><th>후기</th><th>평점</th>
          </tr>
        </thead>
        <tbody>
          {shelterStats.map((s, idx) => (
            <tr key={s.name} style={{ background: idx%2===0 ? '#fff' : '#f7f7f7' }}>
              <td style={{ fontWeight: 700, color: '#222' }}>{s.name}</td>
              <td><ProgressBar percent={Math.round((s.walks/120)*100)} color="#6495ED" />{s.walks}</td>
              <td><ProgressBar percent={Math.round((s.adoptions/32)*100)} color="#6495ED" />{s.adoptions}</td>
              <td><ProgressBar percent={Math.round((s.reviews/14)*100)} color="#6495ED" />{s.reviews}</td>
              <td style={{ fontWeight: 700, color: s.rating>=4.5 ? '#28A745' : '#888' }}>{s.rating}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div style={{marginTop:8, fontSize:'0.95em', color:'#888'}}>※ 비활성 보호소(30일 활동 없음)는 별도 표시 예정</div>
    </section>
  );
}

export default ShelterComparisonStatistics;
