import React from 'react';
import { FaClipboardList, FaCheckCircle, FaTimesCircle, FaDog, FaChartLine } from "react-icons/fa";
import { PieChart, Pie, Cell, ResponsiveContainer, LineChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, Line } from 'recharts';
import StatCard from './common/StatCard';
import ProgressBar from './common/ProgressBar';

function AdoptionStatistics() {
  // 입양 관련 mock 데이터
  const adoptionStats = {
    total: 431,
    approved: 312,
    rejected: 119,
    avgPerDog: 1.7,
    avgDays: 12.4,
    trend: [
      { name: '1월', count: 30 },
      { name: '2월', count: 42 },
      { name: '3월', count: 38 },
      { name: '4월', count: 51 },
      { name: '5월', count: 60 },
    ],
  };
  const approvalRate = Math.round((adoptionStats.approved / adoptionStats.total) * 100);
  const rejectRate = Math.round((adoptionStats.rejected / adoptionStats.total) * 100);
  return (
    <section className="adoption-section walk-section">
      <div className="summary-cards" style={{ marginBottom: 18, justifyContent: 'center' }}>
        <StatCard icon={<FaClipboardList />} label="전체 신청" value={adoptionStats.total + '건'} color="#28A745" />
        <StatCard icon={<FaCheckCircle />} label="승인" value={adoptionStats.approved + '건'} color="#6495ED" />
        <StatCard icon={<FaTimesCircle />} label="거절" value={adoptionStats.rejected + '건'} color="#DC3545" />
        <StatCard icon={<FaDog />} label="평균 신청/유기견" value={`${adoptionStats.avgPerDog}`} color="#FFC107" />
        <StatCard icon={<FaChartLine />} label="평균 소요(일)" value={adoptionStats.avgDays} color="#8A2BE2" />
      </div>
      <div style={{ display: 'flex', gap: 32, flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ minWidth: 180, flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ fontWeight: 600, marginBottom: 6, textAlign: 'center' }}>승인/거절 비율</div>
          <PieChart width={160} height={160}>
            <Pie data={[
              { name: '승인', value: adoptionStats.approved },
              { name: '거절', value: adoptionStats.rejected },
            ]} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={60} label>
              <Cell fill="#6495ED" />
              <Cell fill="#DC3545" />
            </Pie>
          </PieChart>
          <div style={{ display: 'flex', gap: 8, fontSize: 13, marginTop: 4, justifyContent: 'center' }}>
            <span style={{ color: '#6495ED' }}>● 승인 {approvalRate}%</span>
            <span style={{ color: '#DC3545' }}>● 거절 {rejectRate}%</span>
          </div>
        </div>
        <div style={{ flex: 2, minWidth: 220 }}>
          <div style={{ fontWeight: 600, marginBottom: 6, textAlign: 'center' }}>입양 신청 추이</div>
          <ResponsiveContainer width="100%" height={120}>
            <LineChart data={adoptionStats.trend}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="count" stroke="#6495ED" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </section>
  );
}

export default AdoptionStatistics;
