import { useEffect, useState } from "react";
// import { getReservations } from "../../api/shelter/reservation";
import { Link } from "react-router-dom";
import '../../styles/shelter/walk/walkReservation.css';
import Pagination from "../../components/shelter/common/Pagination";

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

function generateDummyReservations(count) {
  const statuses = ["PENDING", "APPROVED", "REJECTED", "CANCELED"];
  const names = ["홍길동", "이순신", "김개발", "박테스터", "최지원", "강감찬", "이하늘", "장보람", "남별이"];
  const dogs = ["초코", "뭉치", "사랑이", "코코", "보리", "누리", "백구", "해피", "두리"];

  const list = [];
  for (let i = 1; i <= count; i++) {
    const randomStatus = statuses[i % statuses.length];
    const randomName = names[i % names.length];
    const randomDog = dogs[i % dogs.length];
    const randomDate = new Date(2025, 5, 15 + (i % 10)); // 2025-06-15 ~ 2025-06-24

    const dateStr = randomDate.toISOString().split("T")[0];
    const startHour = 9 + (i % 6); // 9시 ~ 14시
    const endHour = startHour + 1;

    list.push({
      id: i,
      dogImage: i % 4 === 0 ? null : `/dummy-dog${(i % 5) + 1}.jpg`,
      dogName: randomDog,
      memberName: randomName,
      memberPhone: `010-${1000 + i}-${2000 + i}`,
      date: dateStr,
      startTime: `${startHour}:00`,
      endTime: `${endHour}:00`,
      walkReservationStatus: randomStatus,
      note: i % 3 === 0 ? "테스트 메모입니다." : "",
    });
  }
  return list;
}

function WalkReservationList() {
  // const sheltersId = localStorage.getItem("shelters_id");
  const [filter, setFilter] = useState("전체");
  const [reservations, setReservations] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const filtered = reservations.filter(r => {
    if (filter === "전체") return true;
    return statusToKor(r.walkReservationStatus) === filter;
  });

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filtered.slice(indexOfFirstItem, indexOfLastItem);
  
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const totalPages = Math.ceil(filtered.length / itemsPerPage);
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);

  useEffect(() => {
    // if (!sheltersId) return;
    // getReservations(sheltersId).then(res => {
    //   const sorted = [...res.data].sort((a, b) => new Date(b.date) - new Date(a.date));
    //   setReservations(sorted);
    // });

    // 더미 데이터
    const generated = generateDummyReservations(100); 
    const sorted = [...generated].sort((a, b) => new Date(b.date) - new Date(a.date));
    setReservations(sorted);
    }, []);

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
          {currentItems.map(r => (
            <li key={r.id} className="walk-item">
              <Link to={`/shelter/walk-reservations/${r.id}`} className="walk-link">
                <div className="walk-main-row">
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
      <Pagination
              totalItems={reservations.length}
              itemPerPage={10}
              currentPage={currentPage}
              onPageChange={setCurrentPage}
            />
    </div>
  );
}

export default WalkReservationList;
