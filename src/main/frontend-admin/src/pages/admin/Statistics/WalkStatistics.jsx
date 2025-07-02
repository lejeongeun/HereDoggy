import React from "react";

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

  return (
    <section className="walk-summary-section">
      <h3>산책 예약 현황</h3>
      <ul className="walk-overview">
        <li>총 예약 수: <strong>{total.toLocaleString()}건</strong></li>
        <li>성공: <strong>{success.toLocaleString()}건</strong></li>
        <li>취소: <strong>{cancelled.toLocaleString()}건</strong></li>
        <li>대기: <strong>{pending.toLocaleString()}건</strong></li>
        <li>예약 성공률: <strong>{successRate}%</strong></li>
      </ul>

      <h4>취소 사유 통계</h4>
      <ul className="cancel-reason-list">
        {Object.entries(cancelReasons).map(([reason, count]) => (
          <li key={reason}>
            <span>{reason}</span>
            <span>{count}건</span>
          </li>
        ))}
      </ul>
    </section>
  );
}

export default WalkStatistics;
