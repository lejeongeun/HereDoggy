import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import {
  getReservationDetail,
  approveReservation,
  rejectReservation,
  approveCancel,
} from "../../../api/shelter/reservation";
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

function WalkReservationDetail() {
  const { id } = useParams();
  const sheltersId = localStorage.getItem("shelters_id");
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  // 데이터 fetch
  const fetchDetail = async () => {
    setLoading(true);
    const res = await getReservationDetail(sheltersId, id);
    setData(res.data);
    setLoading(false);
  };

  useEffect(() => {
    fetchDetail();
    // eslint-disable-next-line
  }, []);

  // 승인
  const handleApprove = async () => {
    await approveReservation(sheltersId, id);
    alert("승인 완료");
    fetchDetail();
  };
  // 거절
  const handleReject = async () => {
    await rejectReservation(sheltersId, id);
    alert("거절 완료");
    fetchDetail();
  };
  // 취소
  const handleCancel = async () => {
    await approveCancel(sheltersId, id);
    alert("예약 취소 완료");
    fetchDetail();
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
          <img src={data.dogImage || "/default-dog.png"} alt={data.dogName} />
          <div>
            <div className="walk-detail-dogname">{data.dogName}</div>
            <div className="walk-detail-doginfo">
              {data.dogStatus && <span>상태: {data.dogStatus}</span>}
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
