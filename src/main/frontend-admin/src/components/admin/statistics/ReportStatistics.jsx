import React from 'react';
import { FaClipboardList, FaCheckCircle, FaTimesCircle } from "react-icons/fa";
import { ResponsiveContainer, BarChart, XAxis, YAxis, Bar } from 'recharts';
import StatCard from './common/StatCard';
import ProgressBar from './common/ProgressBar';

function ReportStatistics() {
  // 제보/신고 관련 mock 데이터
  const reportStats = {
    total: 139,
    pending: 23,
    done: 116,
    shelterRank: [
      { name: '행복보호소', count: 32 },
      { name: '희망쉼터', count: 28 },
      { name: '사랑의집', count: 21 },
      { name: '푸른쉼터', count: 18 },
      { name: '햇살보호소', count: 14 },
    ],
    locationHotspots: [
      // 예시: 위도, 경도, count
      { lat: 37.5, lng: 127.1, count: 12 },
      { lat: 37.6, lng: 127.0, count: 8 },
    ],
  };
  const doneRate = Math.round((reportStats.done / reportStats.total) * 100);
  return (
    <section className="report-section">
      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', marginBottom: 18 }}>
        <StatCard icon={<FaClipboardList />} label="전체 제보" value={reportStats.total + '건'} color="#28A745" />
        <StatCard icon={<FaCheckCircle />} label="처리 완료" value={reportStats.done + '건'} color="#6495ED" />
        <StatCard icon={<FaTimesCircle />} label="대기" value={reportStats.pending + '건'} color="#FFC107" />
      </div>
      <div style={{ maxWidth: 340, marginBottom: 18 }}>
        <div style={{ fontWeight: 600, marginBottom: 6 }}>제보 처리율</div>
        <ProgressBar percent={doneRate} color="#28A745" />
        <div style={{ fontSize: 13, color: '#28A745', fontWeight: 600 }}>{doneRate}% 처리 완료</div>
      </div>
      <div style={{ fontWeight: 600, margin: '18px 0 8px 0' }}>보호소별 제보 처리 Top 5</div>
      <ResponsiveContainer width="100%" height={180}>
        <BarChart data={reportStats.shelterRank} layout="vertical">
          <XAxis type="number" hide />
          <YAxis dataKey="name" type="category" width={90} />
          <Bar dataKey="count" fill="#6495ED" barSize={18} radius={[8,8,8,8]} />
        </BarChart>
      </ResponsiveContainer>
      <div style={{marginTop:8, fontSize:'0.95em', color:'#888'}}>※ 지도 기반 Hotspot 시각화는 추후 구현</div>
    </section>
  );
}

export default ReportStatistics;
