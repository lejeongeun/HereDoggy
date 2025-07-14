import React, { useState } from "react";
import {
  FaUsers,
  FaHospitalAlt,
  FaDog,
  FaWalking,
  FaEnvelopeOpenText,
  FaExclamationTriangle,
  FaComment,
} from "react-icons/fa";
import '../../../styles/admin/adminStatistics/adminStatistics.css';
import WalkStatistics from '../../../components/admin/statistics/WalkStatistics';
import DashCard from '../../../components/admin/statistics/common/DashCard';
import AdoptionStatistics from '../../../components/admin/statistics/AdoptionStatistics';
import ReportStatistics from '../../../components/admin/statistics/ReportStatistics';
import ReviewStatistics from '../../../components/admin/statistics/ReviewStatistics';
import ShelterComparisonStatistics from '../../../components/admin/statistics/ShelterComparisonStatistics';
import UserBehaviorStatistics from '../../../components/admin/statistics/UserBehaviorStatistics';

const summaryStats = {
  totalUsers: {
    value: 5683,
    diff: "+2.1%",
    trendData: [
      { day: 'D-6', count: 800 },
      { day: 'D-5', count: 820 },
      { day: 'D-4', count: 810 },
      { day: 'D-3', count: 850 },
      { day: 'D-2', count: 830 },
      { day: 'D-1', count: 880 },
      { day: 'Today', count: 900 },
    ],
  },
  totalShelters: {
    value: 135,
    diff: "+0.7%",
    trendData: [
      { day: 'D-6', count: 10 },
      { day: 'D-5', count: 12 },
      { day: 'D-4', count: 11 },
      { day: 'D-3', count: 13 },
      { day: 'D-2', count: 12 },
      { day: 'D-1', count: 14 },
      { day: 'Today', count: 15 },
    ],
  },
  totalDogs: {
    value: 902,
    diff: "+1.8%",
    trendData: [
      { day: 'D-6', count: 100 },
      { day: 'D-5', count: 105 },
      { day: 'D-4', count: 102 },
      { day: 'D-3', count: 108 },
      { day: 'D-2', count: 106 },
      { day: 'D-1', count: 110 },
      { day: 'Today', count: 112 },
    ],
  },
  totalWalks: {
    value: 7036,
    value7Days: 150,
    diff: "+3.5%",
    trendData: [
      { day: 'D-6', count: 18 },
      { day: 'D-5', count: 22 },
      { day: 'D-4', count: 20 },
      { day: 'D-3', count: 25 },
      { day: 'D-2', count: 23 },
      { day: 'D-1', count: 28 },
      { day: 'Today', count: 30 },
    ],
  },
  totalAdoptions: {
    value: 431,
    value7Days: 25,
    diff: "+1.2%",
    trendData: [
      { day: 'D-6', count: 3 },
      { day: 'D-5', count: 4 },
      { day: 'D-4', count: 5 },
      { day: 'D-3', count: 3 },
      { day: 'D-2', count: 4 },
      { day: 'D-1', count: 6 },
      { day: 'Today', count: 5 },
    ],
  },
  totalReports: {
    value: 139,
    value7Days: 10,
    diff: "-0.6%",
    trendData: [
      { day: 'D-6', count: 12 },
      { day: 'D-5', count: 15 },
      { day: 'D-4', count: 13 },
      { day: 'D-3', count: 16 },
      { day: 'D-2', count: 14 },
      { day: 'D-1', count: 17 },
      { day: 'Today', count: 15 },
    ],
  },
  totalReviews: {
    value: 210,
    diff: "+0.4%",
    trendData: [
      { day: 'D-6', count: 10 },
      { day: 'D-5', count: 12 },
      { day: 'D-4', count: 11 },
      { day: 'D-3', count: 13 },
      { day: 'D-2', count: 12 },
      { day: 'D-1', count: 14 },
      { day: 'Today', count: 15 },
    ],
  },
};

function AdminStatistics() {
  const [tab, setTab] = useState('summary');
  return (
    <div className="statistics-page">
      <h2 className="page-title">시스템 통계 대시보드</h2>
      <div className="statistics-tab-bar">
        <button className={tab==='summary' ? 'active' : ''} onClick={()=>setTab('summary')}>전체 요약</button>
        <button className={tab==='walk' ? 'active' : ''} onClick={()=>setTab('walk')}>산책</button>
        <button className={tab==='adoption' ? 'active' : ''} onClick={()=>setTab('adoption')}>입양</button>
        <button className={tab==='report' ? 'active' : ''} onClick={()=>setTab('report')}>제보/신고</button>
        <button className={tab==='review' ? 'active' : ''} onClick={()=>setTab('review')}>후기</button>
        <button className={tab==='shelter' ? 'active' : ''} onClick={()=>setTab('shelter')}>보호소 비교</button>
        <button className={tab==='user' ? 'active' : ''} onClick={()=>setTab('user')}>사용자 활동</button>
      </div>
      {tab === 'summary' && (
        <section className="summary-section">
          <DashCard label="전체 회원" value={`${summaryStats.totalUsers.value.toLocaleString()}명`} diff={summaryStats.totalUsers.diff} icon={<FaUsers />} trendData={summaryStats.totalUsers.trendData} />
          <DashCard label="보호소 수" value={`${summaryStats.totalShelters.value}곳`} diff={summaryStats.totalShelters.diff} icon={<FaHospitalAlt />} trendData={summaryStats.totalShelters.trendData} />
          <DashCard label="등록 유기견" value={`${summaryStats.totalDogs.value.toLocaleString()}마리`} diff={summaryStats.totalDogs.diff} icon={<FaDog />} trendData={summaryStats.totalDogs.trendData} />
          <DashCard
            label="산책 예약수"
            value={`${summaryStats.totalWalks.value.toLocaleString()}건`}
            tooltipContent={`7일간 ${summaryStats.totalWalks.value7Days.toLocaleString()}건`}
            diff={summaryStats.totalWalks.diff}
            icon={<FaWalking />}
            trendData={summaryStats.totalWalks.trendData}
          />
          <DashCard
            label="입양 신청"
            value={`${summaryStats.totalAdoptions.value.toLocaleString()}건`}
            tooltipContent={`7일간 ${summaryStats.totalAdoptions.value7Days.toLocaleString()}건`}
            diff={summaryStats.totalAdoptions.diff}
            icon={<FaEnvelopeOpenText />}
            trendData={summaryStats.totalAdoptions.trendData}
          />
          <DashCard
            label="신고 건수"
            value={`${summaryStats.totalReports.value.toLocaleString()}건`}
            tooltipContent={`7일간 ${summaryStats.totalReports.value7Days.toLocaleString()}건`}
            diff={summaryStats.totalReports.diff}
            icon={<FaExclamationTriangle />}
            trendData={summaryStats.totalReports.trendData}
          />
          <DashCard label="후기 수" value={`${summaryStats.totalReviews.value}건`} diff={summaryStats.totalReviews.diff} icon={<FaComment />} trendData={summaryStats.totalReviews.trendData} />
        </section>
      )}
      {tab === 'walk' && <WalkStatistics />}
      {tab === 'adoption' && <AdoptionStatistics />}
      {tab === 'report' && <ReportStatistics />}
      {tab === 'review' && <ReviewStatistics />}
      {tab === 'shelter' && <ShelterComparisonStatistics />}
      {tab === 'user' && <UserBehaviorStatistics />}
    </div>
  );
}

export default AdminStatistics;