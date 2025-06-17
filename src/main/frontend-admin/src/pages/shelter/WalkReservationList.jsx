import { useEffect, useState } from "react";
import { getReservations } from "../../api/shelter/reservation";
import { Link } from "react-router-dom";
import '../../styles/shelter/walk/walkReservation.css';

// 날짜 차이 계산 (D-day용)
function getDday(dateStr) {
  const today = new Date();
  const d = new Date(dateStr);
  d.setHours(0, 0, 0, 0); today.setHours(0, 0, 0, 0);
  const diff = Math.floor((d - today) / (1000 * 60 * 60 * 24));
  if (diff === 0) return 'D-day';
  if (diff > 0) return `D-${diff}`;
  return `D+${Math.abs(diff)}`;
}

// 상태 한글 변환
function statusToKor(status) {
  switch (status) {
    case "PENDING": return "대기중";
    case "APPROVED": return "승인됨";
    case "REJECTED": return "거절됨";
    case "CANCELED": return "취소됨";
    default: return status;
  }
}

function WalkReservationList() {
  const sheltersId = localStorage.getItem("shelters_id");
  const [reservations, setReservations] = useState([]);
  const [filter, setFilter] = useState("전체");

  useEffect(() => {
    if (!sheltersId) return;
    getReservations(sheltersId).then(res => {
      const sorted = [...res.data].sort((a, b) => new Date(b.date) - new Date(a.date));
      setReservations(sorted);
    });
  }, [sheltersId]);

  // 필터링된 예약 목록
  const filtered = reservations.filter(r => {
    if (filter === "전체") return true;
    return statusToKor(r.walkReservationStatus) === filter;
  });

  return (
    <div className="walk-container">
      <h2 className="walk-title">산책 예약 목록</h2>

      {/* 상태 필터 드롭다운 */}
      <div style={{ marginBottom: "20px" }}>
        <label htmlFor="status-filter" style={{ marginRight: "12px", fontWeight: "600" }}>상태 필터:</label>
        <select
          id="status-filter"
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          style={{
            padding: "6px 12px",
            borderRadius: "8px",
            border: "1.5px solid #ccc",
            fontSize: "1rem"
          }}
        >
          {["전체", "대기중", "승인됨", "거절됨", "취소됨"].map(option => (
            <option key={option} value={option}>{option}</option>
          ))}
        </select>
      </div>

      {filtered.length === 0 ? (
        <p className="walk-empty">예약이 없습니다.</p>
      ) : (
        <ul className="walk-list">
          {filtered.map(r => (
            <li key={r.id} className="walk-item">
              <Link to={`/shelter/walk-reservations/${r.id}`} className="walk-link">
                <div className="walk-main-row">
                  <div className="walk-thumb">
                    <img src={r.dogImage || "/default-dog.png"} alt={r.dogName} />
                  </div>
                  <div className="walk-info">
                    <div className="walk-info-top">
                      <span className="walk-dog">{r.dogName}</span>
                      <span className="walk-dot">·</span>
                      <span className="walk-member">신청자 : {r.memberName}</span>
                      <span className={`walk-status walk-status--${r.walkReservationStatus?.toLowerCase()}`}>
                        {statusToKor(r.walkReservationStatus)}
                      </span>
                    </div>
                    <div className="walk-meta">
                      <span className="walk-date">{r.date} {r.startTime}~{r.endTime}</span>
                      <span className="walk-dot">·</span>
                      <span className="walk-dday">{getDday(r.date)}</span>
                    </div>
                    <div className="walk-meta-detail">
                      <span className="walk-phone">연락처: {r.memberPhone}</span>
                      {r.note && (
                        <>
                          <span className="walk-dot">·</span>
                          <span className="walk-note">메모: {r.note}</span>
                        </>
                      )}
                    </div>
                  </div>
                </div>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default WalkReservationList;
