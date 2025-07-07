import React from "react";
import { Bar, Line } from "react-chartjs-2";
import 'chart.js/auto';
import '../../styles/admin/adminDashboard/adminDashboard.css';

// FontAwesome 아이콘
import {
  FaUsers,
  FaHospitalAlt,
  FaWalking,
  FaDonate,
  FaExclamationTriangle,
  FaEnvelopeOpenText,
  FaChartLine,
  FaChartBar,
} from "react-icons/fa";

// 더미 데이터
const stats = {
  users: { total: 5683, diff: "+2.1%" },
  shelters: { total: 135, diff: "+0.7%" },
  walks: { total: 7036, diff: "+3.5%" },
  donations: { total: 152000000, diff: "-1.2%" },
  reports: 3,
  inquiries: 1,
};

const donationsLineData = {
  labels: ["3월", "4월", "5월", "6월", "7월", "8월"],
  datasets: [
    {
      label: "후원 금액(원)",
      data: [2300000, 2600000, 2400000, 3000000, 3200000, 2500000],
      tension: 0.4,
      fill: false,
      borderColor: "#2a9d8f",
      pointBackgroundColor: "#2a9d8f",
    }
  ]
};

const donationsLineOptions = {
  maintainAspectRatio: false,
  scales: {
    y: {
      title: { display: true, text: "금액 (원)" },
      ticks: {
        callback: (value) => value.toLocaleString() + "원"
      }
    }
  },
  plugins: {
    tooltip: {
      callbacks: {
        label: (ctx) => `${ctx.raw.toLocaleString()}원`
      }
    }
  }
};

const shelterBarOptions = {
  maintainAspectRatio: false,
  layout: {
    padding: 0
  },
  plugins: {
    legend: { display: false },
  },
  scales: {
    x: {
      ticks: {
        maxRotation: 0,
        autoSkip: false,
      }
    },
    y: {
      beginAtZero: true,
      title: {
        display: true,
        text: "예약 건수"
      },
      ticks: {
        stepSize: 100
      }
    }
  }
};

const shelterBarData = {
  labels: ["행복보호소", "희망보호소", "해피펫", "러브독", "강아지세상"],
  datasets: [
    {
      label: "산책 예약 건수",
      data: [701, 623, 589, 502, 480],
      backgroundColor: "#457b9d",
      categoryPercentage: 0.4,
      barPercentage: 0.8,
    }
  ]
};


function AdminDashboard() {
  return (
    <div className="dashboard-container">
      <div className="dashboard-cards">
        <DashCard label="전체 회원" value={`${stats.users.total.toLocaleString()}명`} diff={stats.users.diff} icon={<FaUsers />} />
        <DashCard label="전체 보호소" value={`${stats.shelters.total}곳`} diff={stats.shelters.diff} icon={<FaHospitalAlt />} />
        <DashCard label="누적 산책" value={`${stats.walks.total.toLocaleString()}건`} diff={stats.walks.diff} icon={<FaWalking />} />
        <DashCard label="누적 후원" value={`${stats.donations.total.toLocaleString()}원`} diff={stats.donations.diff} icon={<FaDonate />} />
        <DashCard label="미처리 신고" value={`${stats.reports}건`} highlight icon={<FaExclamationTriangle />} />
        <DashCard label="미답변 문의" value={`${stats.inquiries}건`} highlight icon={<FaEnvelopeOpenText />} />
      </div>

      <div className="dashboard-charts">
        <div className="dashboard-chart-box">
          <h3 className="chart-title"><FaChartLine style={{ marginRight: 6 }} />월별 전체 후원 금액</h3>
          <div className="chart-canvas-container">
            <Line data={donationsLineData} options={donationsLineOptions} />
          </div>
        </div>
        <div className="dashboard-chart-box">
          <h3 className="chart-title"><FaChartBar style={{ marginRight: 6 }} />보호소별 산책 예약 TOP5</h3>
          <div className="chart-canvas-container">
            <Bar data={shelterBarData} options={shelterBarOptions} />
          </div>
        </div>
      </div>
    </div>
  );
}

// 카드 컴포넌트
function DashCard({ label, value, diff, highlight, icon }) {
  return (
    <div className={`dash-card ${highlight ? 'highlight-card' : ''}`}>
      <div className="dash-card-top">
        <span className="dash-card-label">{label}</span>
        <span className="dash-card-icon">{icon}</span>
      </div>
      <div className="dash-card-value">{value}</div>
      {diff && (
        <div className={`dash-card-diff ${diff.startsWith('+') ? 'up' : 'down'}`}>
          {diff}
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;
