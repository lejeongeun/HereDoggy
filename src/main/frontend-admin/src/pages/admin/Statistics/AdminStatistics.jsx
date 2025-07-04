// AdminStatistics.jsx
import React from "react";
import {
  FaUsers,
  FaHospitalAlt,
  FaDog,
  FaWalking,
  FaEnvelopeOpenText,
  FaExclamationTriangle,
  FaComment
} from "react-icons/fa";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, BarChart, Bar } from 'recharts';
import '../../../styles/admin/adminStatistics/adminStatistics.css';
import WalkStatistics from './WalkStatistics';

const summaryStats = {
  totalUsers: { value: 5683, diff: "+2.1%" },
  totalShelters: { value: 135, diff: "+0.7%" },
  totalDogs: { value: 902, diff: "+1.8%" },
  totalWalks: { value: 7036, diff: "+3.5%" },
  totalAdoptions: { value: 431, diff: "+1.2%" },
  totalReports: { value: 139, diff: "-0.6%" },
  totalReviews: { value: 210, diff: "+0.4%" }
};

const walkReservationData = [
  { name: '월', '예약 건수': 400, '취소 건수': 24 },
  { name: '화', '예약 건수': 300, '취소 건수': 13 },
  { name: '수', '예약 건수': 200, '취소 건수': 98 },
  { name: '목', '예약 건수': 278, '취소 건수': 39 },
  { name: '금', '예약 건수': 189, '취소 건수': 48 },
  { name: '토', '예약 건수': 239, '취소 건수': 38 },
  { name: '일', '예약 건수': 349, '취소 건수': 43 },
];

const cancellationReasonData = [
  { name: '단순 변심', '건수': 120 },
  { name: '일정 변경', '건수': 90 },
  { name: '날씨 문제', '건수': 60 },
  { name: '기타', '건수': 30 },
];

function AdminStatistics() {
  return (
    <div className="statistics-page">
      <h2 className="page-title">시스템 통계 대시보드</h2>

      {/* 요약 카드 */}
      <section className="summary-section">
        <DashCard label="전체 회원" value={`${summaryStats.totalUsers.value.toLocaleString()}명`} diff={summaryStats.totalUsers.diff} icon={<FaUsers />} />
        <DashCard label="보호소 수" value={`${summaryStats.totalShelters.value}곳`} diff={summaryStats.totalShelters.diff} icon={<FaHospitalAlt />} />
        <DashCard label="등록 유기견" value={`${summaryStats.totalDogs.value.toLocaleString()}마리`} diff={summaryStats.totalDogs.diff} icon={<FaDog />} />
        <DashCard label="산책 예약" value={`${summaryStats.totalWalks.value.toLocaleString()}건`} diff={summaryStats.totalWalks.diff} icon={<FaWalking />} />
        <DashCard label="입양 신청" value={`${summaryStats.totalAdoptions.value.toLocaleString()}건`} diff={summaryStats.totalAdoptions.diff} icon={<FaEnvelopeOpenText />} />
        <DashCard label="신고 건수" value={`${summaryStats.totalReports.value}건`} diff={summaryStats.totalReports.diff} icon={<FaExclamationTriangle />} />
        <DashCard label="후기 수" value={`${summaryStats.totalReviews.value}건`} diff={summaryStats.totalReviews.diff} icon={<FaComment />} />
      </section>

      {/* 산책 통계 */}
      <WalkStatistics />

      {/* 차트 섹션 */}
      <section className="charts-section">
        <div className="chart-container">
          <h3 className="chart-title">주간 산책 예약 현황</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={walkReservationData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="예약 건수" stroke="#8884d8" activeDot={{ r: 8 }} />
              <Line type="monotone" dataKey="취소 건수" stroke="#82ca9d" />
            </LineChart>
          </ResponsiveContainer>
        </div>
        <div className="chart-container">
          <h3 className="chart-title">산책 취소 사유</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={cancellationReasonData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="건수" fill="#8884d8" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </section>
    </div>
  );
}

function DashCard({ label, value, diff, icon }) {
  return (
    <div className="dash-card">
      <div className="dash-card-top">
        <span className="dash-card-label">{label}</span>
        <span className="dash-card-icon">{icon}</span>
      </div>
      <div className="dash-card-value">{value}</div>
      {diff && (
        <div className={`dash-card-diff ${diff.startsWith('+') ? 'up' : 'down'}`}>{diff}</div>
      )}
    </div>
  );
}

export default AdminStatistics;
