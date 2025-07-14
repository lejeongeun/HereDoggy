import React from 'react';
import { FaUserFriends, FaBell, FaChartLine } from "react-icons/fa";
import { ResponsiveContainer, LineChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, Line } from 'recharts';
import StatCard from './common/StatCard';

function UserBehaviorStatistics() {
  // 사용자 행동 패턴 mock 데이터
  const userStats = {
    signupTrend: [
      { name: '1월', count: 120 },
      { name: '2월', count: 210 },
      { name: '3월', count: 160 },
      { name: '4월', count: 245 },
      { name: '5월', count: 330 },
    ],
    mau: 1200,
    wau: 430,
    notificationOptIn: 0.82,
    pushClickRate: 0.37,
  };
  return (
    <section className="user-behavior-section">
      <div className="summary-cards">
        <StatCard icon={<FaUserFriends />} label="월간 활성(MAU)" value={userStats.mau + '명'} color="#28A745" />
        <StatCard icon={<FaUserFriends />} label="주간 활성(WAU)" value={userStats.wau + '명'} color="#6495ED" />
        <StatCard icon={<FaBell />} label="알림 동의율" value={(userStats.notificationOptIn*100).toFixed(1)+'%'} color="#FFC107" />
        <StatCard icon={<FaChartLine />} label="푸시 클릭률" value={(userStats.pushClickRate*100).toFixed(1)+'%'} color="#8A2BE2" />
      </div>
      <div style={{ fontWeight: 600, margin: '18px 0 8px 0' }}>회원 가입 추이</div>
      <ResponsiveContainer width="100%" height={120}>
        <LineChart data={userStats.signupTrend}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="count" stroke="#6495ED" />
        </LineChart>
      </ResponsiveContainer>
    </section>
  );
}

export default UserBehaviorStatistics;
