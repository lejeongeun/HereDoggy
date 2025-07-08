import { useState } from "react";
import { Link } from "react-router-dom";
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

// ✅ 더미 데이터 생성 함수
function generateDummyAdoptions(count) {
  const names = ["홍길동", "김영희", "이철수", "박영수", "최은주"];
  const dogs = ["행복이", "초코", "사랑이", "두부", "보리"];
  const statuses = ["PENDING", "APPROVED", "REJECTED"];

  const list = [];
  for (let i = 1; i <= count; i++) {
    const name = names[i % names.length];
    const dog = dogs[i % dogs.length];
    const status = statuses[i % statuses.length];

    const visitDate = new Date(2024, 5, 10 + (i % 10)); // 6월 10~19일
    const visitTime = `${10 + (i % 5)}:00:00`; // 10시 ~ 14시

    list.push({
      adoptionId: i,
      memberName: name,
      memberPhone: `010-${1000 + i}-${2000 + i}`,
      dogName: dog,
      visitDate: visitDate.toISOString().split("T")[0],
      visitTime,
      createdAt: new Date(2024, 5, 1 + (i % 5)).toISOString(),
      status,
    });
  }
  return list;
}

function AdoptionList() {
  const [adoptions] = useState(generateDummyAdoptions(25)); // 배열로 초기화
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

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