import React from "react";
import { Bar, Line } from "react-chartjs-2";
import 'chart.js/auto';

// 더미 데이터
const stats = {
  users: 5683,
  shelters: 135,
  walks: 7036,
  donations: 152000000,
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

const shelterBarData = {
  labels: ["행복보호소", "희망보호소", "해피펫", "러브독", "강아지세상"],
  datasets: [
    {
      label: "산책 예약 건수",
      data: [701, 623, 589, 502, 480],
      backgroundColor: "#457b9d",
    }
  ]
};

function AdminDashboard() {
  return (
    <div style={{ padding: 32, background: "#f6faf7", minHeight: "100vh" }}>
      {/* 요약 카드 */}
      <div style={{ display: "flex", gap: 24, marginBottom: 32 }}>
        <DashCard label="전체 회원" value={stats.users + "명"} />
        <DashCard label="전체 보호소" value={stats.shelters + "곳"} />
        <DashCard label="누적 산책" value={stats.walks + "건"} />
        <DashCard label="누적 후원" value={stats.donations.toLocaleString() + "원"} />
      </div>

      {/* 그래프 2개 */}
      <div style={{ display: "flex", gap: 32 }}>
        <div style={{ flex: 1, background: "#fff", padding: 24, borderRadius: 16 }}>
          <h3 style={{ marginBottom: 16 }}>월별 전체 후원 금액</h3>
          <Line data={donationsLineData} height={210} />
        </div>
        <div style={{ flex: 1, background: "#fff", padding: 24, borderRadius: 16 }}>
          <h3 style={{ marginBottom: 16 }}>보호소별 산책 예약 TOP5</h3>
          <Bar data={shelterBarData} height={210} />
        </div>
      </div>
    </div>
  );
}

// 요약 카드 컴포넌트
function DashCard({ label, value }) {
  return (
    <div style={{
      background: "#fff",
      borderRadius: 14,
      boxShadow: "0 1px 6px #eee",
      padding: "22px 36px",
      minWidth: 140,
      flex: 1
    }}>
      <div style={{ fontSize: 16, color: "#888" }}>{label}</div>
      <div style={{ fontWeight: 700, fontSize: 28, marginTop: 5 }}>{value}</div>
    </div>
  );
}

export default AdminDashboard;
