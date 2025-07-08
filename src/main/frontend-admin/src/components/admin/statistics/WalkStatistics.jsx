import React from "react";
import { FaWalking, FaCheckCircle, FaTimesCircle, FaClock, FaChartPie } from "react-icons/fa";
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import CountUp from 'react-countup';

function StatCard({ icon, label, value, color }) {
  return (
    <div style={{
      display: 'flex', alignItems: 'center', background: '#f9f9f9', borderRadius: 12, padding: '18px 20px', boxShadow: '0 2px 8px rgba(44,62,80,0.06)', marginRight: 16, marginBottom: 12
    }}>
      <span style={{ fontSize: 28, color, marginRight: 14 }}>{icon}</span>
      <div>
        <div style={{ fontSize: 13, color: '#888', fontWeight: 600 }}>{label}</div>
        <div style={{ fontSize: 22, fontWeight: 800, color: '#222' }}>
          {value}
        </div>
      </div>
    </div>
  );
}

function ProgressBar({ percent, color }) {
  return (
    <div style={{ background: '#eee', borderRadius: 8, height: 12, width: '100%', margin: '6px 0' }}>
      <div style={{ width: `${percent}%`, background: color, height: '100%', borderRadius: 8, transition: 'width 0.4s' }} />
    </div>
  );
}

const COLORS = ['#23b266', '#e67e22', '#3498db', '#f39c12', '#e74c3c'];

function WalkStatistics() {
  const walkStats = {
    total: 7036,
    success: 6120,
    cancelled: 631,
    pending: 285,
    cancelReasons: {
      "개인 사정": 320,
      "날씨 문제": 190,
      "보호소 요청": 121,
    },
  };

  const { total, success, cancelled, pending, cancelReasons } = walkStats;
  const successRate = ((success / total) * 100).toFixed(1);
  const cancelRate = ((cancelled / total) * 100).toFixed(1);
  const pendingRate = ((pending / total) * 100).toFixed(1);

  const cancelReasonArr = Object.entries(cancelReasons).map(([name, value]) => ({ name, value }));

  return (
    <section className="walk-section walk-summary-section">
      <div className="summary-cards">
        <StatCard icon={<FaWalking />} label="총 예약" value={total.toLocaleString() + '건'} color="#23b266" />
        <StatCard icon={<FaCheckCircle />} label="성공" value={success.toLocaleString() + '건'} color="#3498db" />
        <StatCard icon={<FaTimesCircle />} label="취소" value={cancelled.toLocaleString() + '건'} color="#e74c3c" />
        <StatCard icon={<FaClock />} label="대기" value={pending.toLocaleString() + '건'} color="#f39c12" />
      </div>
      <div className="walk-progress">
        <div className="walk-progress-label">예약 성공률</div>
        <ProgressBar percent={successRate} color="#23b266" />
        <div style={{ fontSize: 13, color: '#23b266', fontWeight: 600 }}>{successRate}% 성공</div>
      </div>
      <div className="walk-graph-row">
        <div className="walk-graph-block">
          <div className="walk-graph-title">취소 사유 분포</div>
          <ResponsiveContainer width="100%" height={180}>
            <BarChart data={cancelReasonArr} layout="vertical" margin={{ top: 10, right: 30, left: 30, bottom: 10 }}>
              <XAxis type="number" tick={{ fontSize: 13 }} />
              <YAxis dataKey="name" type="category" width={90} tick={{ fontSize: 13 }} />
              <Bar dataKey="value" fill="#e67e22" barSize={18} radius={[8,8,8,8]} label={{ position: 'right', fontSize: 13 }}>
                {cancelReasonArr.map((entry, idx) => (
                  <Cell key={entry.name} fill={COLORS[idx % COLORS.length]} />
                ))}
              </Bar>
              <Tooltip />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <div className="walk-graph-block">
          <div className="walk-graph-title">상태 비율</div>
          <PieChart width={200} height={200}>
            <Pie
              data={[
                { name: '성공', value: success },
                { name: '취소', value: cancelled },
                { name: '대기', value: pending },
              ]}
              dataKey="value"
              nameKey="name"
              cx="50%"
              cy="50%"
              outerRadius={50}
              label
              >
              <Cell fill="#3498db" />
              <Cell fill="#e74c3c" />
              <Cell fill="#f39c12" />
            </Pie>
          </PieChart>
          <div style={{ display: 'flex', gap: 8, fontSize: 13, marginTop: 4 }}>
            <span style={{ color: '#3498db' }}>● 성공 {successRate}%</span>
            <span style={{ color: '#e74c3c' }}>● 취소 {cancelRate}%</span>
            <span style={{ color: '#f39c12' }}>● 대기 {pendingRate}%</span>
          </div>
        </div>
      </div>
    </section>
  );
}

export default WalkStatistics;
