import { useEffect, useState } from "react";
import { getReservations } from "../../api/shelter/reservation";
import { Link } from "react-router-dom";
import '../../styles/shelter/walk/walkReservation.css';

function WalkReservationList() {
  const sheltersId = localStorage.getItem("shelters_id");
  const [reservations, setReservations] = useState([]);

  useEffect(() => {
    if (!sheltersId) return;
    getReservations(sheltersId).then(res => setReservations(res.data));
  }, [sheltersId]);

  return (
    <div className="walk-container">
      <h2 className="walk-title">산책 예약 목록</h2>
      {reservations.length === 0 ? (
        <p className="walk-empty">예약이 없습니다.</p>
      ) : (
        <ul className="walk-list">
          {reservations.map(r => (
            <li key={r.id} className="walk-item">
              <Link to={`/shelter/walk-reservations/${r.id}`} className="walk-link">
                <div>
                  <span className="walk-member">{r.memberName}</span>
                  <span className="walk-dot">·</span>
                  <span className="walk-dog">{r.dogName}</span>
                </div>
                <div className="walk-date">
                  {r.date} {r.startTime}~{r.endTime}
                </div>
                <span className={`walk-status walk-status--${r.status?.toLowerCase()}`}>
                  {statusToKor(r.status)}
                </span>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

// 상태 한글 변환 함수
function statusToKor(status) {
  switch (status) {
    case "PENDING": return "대기중";
    case "APPROVED": return "승인됨";
    case "REJECTED": return "거절됨";
    case "CANCELED": return "취소됨";
    default: return status;
  }
}

export default WalkReservationList;
