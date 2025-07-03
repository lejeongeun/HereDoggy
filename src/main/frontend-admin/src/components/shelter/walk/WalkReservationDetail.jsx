import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
// import {
//   getReservationDetail,
//   approveReservation,
//   rejectReservation,
//   approveCancel,
// } from "../../../api/shelter/reservation";
import "../../../styles/shelter/walk/walkReservationDetail.css";

function statusToKor(status) {
  switch (status) {
    case "PENDING": return "대기중";
    case "APPROVED": return "승인됨";
    case "REJECTED": return "거절됨";
    case "CANCELED": return "취소됨";
    default: return status;
  }
}

function getDday(dateStr) {
  const today = new Date();
  const d = new Date(dateStr);
  d.setHours(0,0,0,0); today.setHours(0,0,0,0);
  const diff = Math.floor((d-today)/(1000*60*60*24));
  if (diff === 0) return 'D-day';
  if (diff > 0) return `D-${diff}`;
  return `D+${Math.abs(diff)}`;
}

// === 더미 데이터 선언 ===
const dummyReservationDetails = {
  1: {
    id: 1,
    dogImage: "/dummy-dog1.jpg",
    dogName: "뭉치",
    breed: "믹스",
    age: "3살",
    dogStatus: "활발함",
    memberName: "홍길동",
    memberPhone: "010-1234-5678",
    memberEmail: "hong@example.com",
    memberWalkCount: 2,
    date: "2025-06-20",
    startTime: "10:00",
    endTime: "11:00",
    walkReservationStatus: "PENDING",
    note: "처음 산책해봐요!",
    createAt: "2025-06-14T13:11:15",
    decisionAt: null,
  },
  2: {
    id: 2,
    dogImage: "/dummy-dog2.jpg",
    dogName: "초코",
    breed: "푸들",
    age: "5살",
    dogStatus: "조용함",
    memberName: "이순신",
    memberPhone: "010-5678-1234",
    memberEmail: "lee@naver.com",
    memberWalkCount: 8,
    date: "2025-06-19",
    startTime: "14:00",
    endTime: "15:00",
    walkReservationStatus: "APPROVED",
    note: "",
    createAt: "2025-06-15T15:21:12",
    decisionAt: "2025-06-17T09:13:10",
  },
  3: {
    id: 3,
    dogImage: null,
    dogName: "보리",
    breed: "진돗개",
    age: "1살",
    dogStatus: "장난꾸러기",
    memberName: "Park Tester",
    memberPhone: "010-0000-0000",
    memberEmail: "park@test.com",
    memberWalkCount: 1,
    date: "2025-06-18",
    startTime: "09:00",
    endTime: "09:30",
    walkReservationStatus: "REJECTED",
    note: "시간 변경 가능할까요?",
    createAt: "2025-06-13T09:02:18",
    decisionAt: "2025-06-15T17:43:21",
  },
  4: {
    id: 4,
    dogImage: null,
    dogName: "모카",
    breed: "말티즈",
    age: "2살",
    memberName: "김취소",
    memberPhone: "010-8888-9999",
    memberEmail: "cancel@example.com",
    memberWalkCount: 0,
    date: "2025-06-21",
    startTime: "16:00",
    endTime: "17:00",
    walkReservationStatus: "CANCELED",
    note: "",
    createAt: "2025-06-14T11:55:10",
    decisionAt: "2025-06-16T11:11:11",
  },
};

function WalkReservationDetail() {
  const { id } = useParams();
  // const sheltersId = localStorage.getItem("shelters_id");
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  // 데이터 fetch (더미로 대체)
  const fetchDetail = async () => {
    setLoading(true);
    // const res = await getReservationDetail(sheltersId, id);
    // setData(res.data);

    // ===== 더미 데이터 사용 =====
    setTimeout(() => {
      setData(dummyReservationDetails[id] || dummyReservationDetails[1]);
      setLoading(false);
    }, 300); // 로딩구현용 딜레이(0.3초)
  };

  useEffect(() => {
    fetchDetail();
    // eslint-disable-next-line
  }, [id]);

  // 승인
  const handleApprove = async () => {
    // await approveReservation(sheltersId, id);
    alert("승인 완료 (더미)");
    // fetchDetail();
  };
  // 거절
  const handleReject = async () => {
    // await rejectReservation(sheltersId, id);
    alert("거절 완료 (더미)");
    // fetchDetail();
  };
  // 취소
  const handleCancel = async () => {
    // await approveCancel(sheltersId, id);
    alert("예약 취소 완료 (더미)");
    // fetchDetail();
  };

  if (loading || !data) return <div className="walk-detail-loading">로딩 중...</div>;

  return (
    <div className="walk-detail-container">
      {/* 상단: 예약 상태, 날짜, D-day */}
      <div className="walk-detail-header">
        <span className={`walk-detail-status walk-status--${data.walkReservationStatus?.toLowerCase()}`}>
          {statusToKor(data.walkReservationStatus)}
        </span>
        <span className="walk-detail-dday">{getDday(data.date)}</span>
        <span className="walk-detail-date">
          {data.date} {data.startTime}~{data.endTime}
        </span>
      </div>

      <div className="walk-detail-main">
        {/* 강아지 정보 */}
        <div className="walk-detail-dog">
          <div>
            <div className="walk-detail-dogname">{data.dogName}</div>
            <div className="walk-detail-doginfo">
              {/* {data.dogStatus && <span>상태: {data.dogStatus}</span>} */}
              {data.breed && <span>· 견종: {data.breed}</span>}
              {data.age && <span>· 나이: {data.age}</span>}
            </div>
          </div>
        </div>
        {/* 예약자 정보 */}
        <div className="walk-detail-member">
          <div><b>예약자:</b> {data.memberName}</div>
          <div><b>연락처:</b> {data.memberPhone}</div>
          <div><b>이메일:</b> {data.memberEmail}</div>
          {data.memberWalkCount && (
            <div><b>누적 산책횟수:</b> {data.memberWalkCount}회</div>
          )}
        </div>
        {/* 예약 상세/메모 */}
        <div className="walk-detail-meta">
          <div><b>예약일시:</b> {data.date} {data.startTime}~{data.endTime}</div>
          <div><b>예약 신청일:</b> {data.createAt?.split("T")[0]}</div>
          {data.decisionAt && <div><b>승인/거절일:</b> {data.decisionAt?.split("T")[0]}</div>}
          <div><b>상태:</b> {statusToKor(data.walkReservationStatus)}</div>
          {data.note && (
            <div className="walk-detail-note"><b>메모:</b> {data.note}</div>
          )}
        </div>
        {/* 관리 액션 버튼 */}
        <div className="walk-detail-actions">
          {data.walkReservationStatus === "PENDING" && (
            <>
              <button className="walk-approve-btn" onClick={handleApprove}>승인</button>
              <button className="walk-reject-btn" onClick={handleReject}>거절</button>
            </>
          )}
          {data.walkReservationStatus === "APPROVED" && (
            <button className="walk-cancel-btn" onClick={handleCancel}>예약 취소</button>
          )}
        </div>
      </div>
    </div>
  );
}

export default WalkReservationDetail;
