import React from "react";
import { Bar } from "react-chartjs-2";
import 'chart.js/auto';

const shelterWalks = [
  { name: "행복보호소", walks: 701 },
  { name: "희망보호소", walks: 623 },
  { name: "해피펫", walks: 589 },
  { name: "러브독", walks: 502 },
  { name: "강아지세상", walks: 480 },
];
// 지난달 데이터 예시 (실제 API 연동시 여기도 연결!)
const shelterWalksPrev = [
  { name: "행복보호소", walks: 670 },
  { name: "희망보호소", walks: 612 },
  { name: "해피펫", walks: 555 },
  { name: "러브독", walks: 480 },
  { name: "강아지세상", walks: 455 },
];

// 증감률 계산 함수
const getDiff = (cur, prev) =>
  prev === 0 ? 0 : Math.round(((cur - prev) / prev) * 100);

const shelterWalkBarData = {
  labels: shelterWalks.map(s => s.name),
  datasets: [
    {
      label: "산책 예약 수",
      data: shelterWalks.map(s => s.walks),
      backgroundColor: "#457b9d",
      borderRadius: 8,
      maxBarThickness: 36,
    }
  ]
};

function ShelterWalksBar() {
  return (
    <div
      style={{
        background: "#fff",
        padding: 18,
        borderRadius: 14,
        minWidth: 270,
        flex: 1,
        boxShadow: "0 2px 8px 0 rgba(36,50,74,0.03)",
        marginBottom: 28,
        height: 320,
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-start"
      }}
    >
      <h3 style={{
        marginBottom: 14,
        fontSize: "1.09rem",
        color: "#24324a",
        fontWeight: 600,
        letterSpacing: "-0.5px"
      }}>
        보호소별 산책 예약 TOP5
      </h3>
      <div style={{ flex: 1 }}>
        <Bar
          data={shelterWalkBarData}
          options={{
            maintainAspectRatio: false,
            plugins: {
              legend: { display: false },
              tooltip: {
                callbacks: {
                  label: ctx => `${ctx.parsed.y}회`
                }
              },
            },
            scales: {
              x: {
                grid: { display: false },
                ticks: { font: { size: 13 } }
              },
              y: {
                beginAtZero: true,
                ticks: { stepSize: 100, font: { size: 13 } },
                grid: { color: "#e3e6ee" }
              }
            }
          }}
        />
      </div>
 <table style={{
        width: "100%",
        marginTop: 18,
        borderCollapse: "collapse",
        fontSize: "0.98rem",
        background: "#f9fbfc"
      }}>
        <thead>
          <tr style={{ background: "#f6faf7" }}>
            <th style={{ padding: 8, borderBottom: "1px solid #e3e6ee", color: "#24324a", fontWeight: 600 }}>보호소명</th>
            <th style={{ padding: 8, borderBottom: "1px solid #e3e6ee", color: "#24324a", fontWeight: 600 }}>산책예약수</th>
            <th style={{ padding: 8, borderBottom: "1px solid #e3e6ee", color: "#24324a", fontWeight: 600 }}>증감(%)</th>
          </tr>
        </thead>
        <tbody>
          {shelterWalks.map((s, idx) => {
            const prev = shelterWalksPrev.find(p => p.name === s.name)?.walks || 0;
            const diff = getDiff(s.walks, prev);
            const isUp = diff > 0;
            return (
              <tr key={s.name}>
                <td style={{ padding: 8, textAlign: "center" }}>{s.name}</td>
                <td style={{ padding: 8, textAlign: "center" }}>{s.walks}</td>
                <td style={{
                  padding: 8,
                  textAlign: "center",
                  color: diff === 0 ? "#999" : isUp ? "#209271" : "#e24545",
                  fontWeight: 500
                }}>
                  {diff === 0 ? "-" : (isUp ? "▲" : "▼") + Math.abs(diff) + "%"}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default ShelterWalksBar;
