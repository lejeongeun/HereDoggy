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