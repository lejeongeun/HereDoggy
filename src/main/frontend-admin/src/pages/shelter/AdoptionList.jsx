import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAdoptions } from "../../api/shelter/adoption";
import "../../styles/shelter/adoption/adoptionList.css";

// 상태 한글 변환
function statusToKor(status) {
  switch (status) {
    case "PENDING": return "대기중";
    case "APPROVED": return "승인됨";
    case "REJECTED": return "거절됨";
    default: return status;
  }
}

// D-day 계산
function getDday(dateStr) {
  const today = new Date();
  const d = new Date(dateStr);
  d.setHours(0,0,0,0); today.setHours(0,0,0,0);
  const diff = Math.floor((d-today)/(1000*60*60*24));
  if (diff === 0) return 'D-day';
  if (diff > 0) return `D-${diff}`;
  return `D+${Math.abs(diff)}`;
}

function AdoptionList() {
  const sheltersId = localStorage.getItem("shelters_id");
  // const [adoptions, setAdoptions] = useState([]);
const [adoptions, setAdoptions] = useState([
    {
      adoptionId: 3,
      memberName: "홍길동",
      memberPhone: "010-1234-5678",
      dogName: "행복이",
      visitDate: "2024-06-15",
      visitTime: "14:00:00",
      createdAt: "2024-06-11T10:30:00",
      status: "PENDING",
    },
    {
      adoptionId: 4,
      memberName: "김희진",
      memberPhone: "010-5555-8888",
      dogName: "초코",
      visitDate: "2024-06-20",
      visitTime: "11:00:00",
      createdAt: "2024-06-10T09:15:00",
      status: "APPROVED",
    }
  ]);
//   useEffect(() => {
//     if (!sheltersId) return;
//     getAdoptions(sheltersId).then(res => setAdoptions(res.data));
//   }, [sheltersId]);

  return (
    <div className="adoption-container">
      <h2 className="adoption-title">입양 신청 목록</h2>
      {adoptions.length === 0 ? (
        <p className="adoption-empty">입양 신청이 없습니다.</p>
      ) : (
        <ul className="adoption-list">
          {adoptions.map(a => (
            <li key={a.adoptionId} className="adoption-item">
              <Link to={`/shelter/adoptions/${a.adoptionId}`} className="adoption-link">
                <div className="adoption-main-row">
                  <div className="adoption-info">
                    <div className="adoption-info-top">
                      <span className="adoption-dog">{a.dogName}</span>
                      <span className="adoption-dot">·</span>
                      <span className="adoption-member">{a.memberName}</span>
                      <span className={`adoption-status adoption-status--${a.status?.toLowerCase()}`}>
                        {statusToKor(a.status)}
                      </span>
                    </div>
                    <div className="adoption-meta">
                      <span className="adoption-date">
                        방문일: {a.visitDate} {a.visitTime}
                      </span>
                      <span className="adoption-dot">·</span>
                      <span className="adoption-phone">{a.memberPhone}</span>
                      <span className="adoption-dot">·</span>
                      <span className="adoption-dday">{getDday(a.visitDate)}</span>
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

export default AdoptionList;
