import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAdoptions } from "../../api/shelter/adoption"; 
import "../../styles/shelter/adoption/adoptionList.css";
import Pagination from "../../components/shelter/common/Pagination";

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
  d.setHours(0, 0, 0, 0);
  today.setHours(0, 0, 0, 0);
  const diff = Math.floor((d - today) / (1000 * 60 * 60 * 24));
  if (diff === 0) return "D-day";
  if (diff > 0) return `D-${diff}`;
  return `D+${Math.abs(diff)}`;
}

function AdoptionList() {
  const sheltersId = localStorage.getItem("shelters_id");
  const [adoptions, setAdoptions] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;


  useEffect(() => {
    if (!sheltersId) return;

    getAdoptions(sheltersId)
      .then((res) => {
        const sorted = [...res.data].sort(
          (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
        );
        setAdoptions(sorted);
      })
      .catch((err) => {
        console.error("입양 신청 목록 조회 실패:", err);
      });
  }, [sheltersId]);


  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAdoptions = adoptions.slice(indexOfFirstItem, indexOfLastItem);

  return (
    <div className="adoption-container">
      <h2 className="adoption-title">입양 신청 목록</h2>
      {adoptions.length === 0 ? (
        <p className="adoption-empty">입양 신청이 없습니다.</p>
      ) : (
        <ul className="adoption-list">
          {currentAdoptions.map((a) => (
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

      <Pagination
        totalItems={adoptions.length}
        itemPerPage={itemsPerPage}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}

export default AdoptionList;
