import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import {
  getAdoptionDetail,
  approveAdoption,
  rejectAdoption,
} from "../../../api/shelter/adoption";
import "../../../styles/shelter/adoption/adoptionDetail.css";

function statusToKor(status) {
  switch (status) {
    case "PENDING": return "대기중";
    case "APPROVED": return "승인됨";
    case "REJECTED": return "거절됨";
    default: return status;
  }
}

function AdoptionDetail() {
//   const { id } = useParams();
//   const sheltersId = localStorage.getItem("shelters_id");
//   const [data, setData] = useState(null);
//   const [loading, setLoading] = useState(true);

//   const fetchDetail = async () => {
//     setLoading(true);
//     const res = await getAdoptionDetail(sheltersId, id);
//     setData(res.data);
//     setLoading(false);
//   };

//   useEffect(() => { fetchDetail(); }, []);

//   const handleApprove = async () => {
//     await approveAdoption(sheltersId, id);
//     alert("입양 승인 완료");
//     fetchDetail();
//   };
//   const handleReject = async () => {
//     await rejectAdoption(sheltersId, id);
//     alert("입양 거절 완료");
//     fetchDetail();
//   };
  const [data] = useState({
    adoptionId: 3,
    dogName: "행복이",
    memberName: "홍길동",
    memberPhone: "010-1234-5678",
    visitDate: "2024-06-15",
    visitTime: "14:00:00",
    createdAt: "2024-06-11T10:30:00",
    status: "PENDING",
    decisionAt: null,
    survey: {
      "입양 이유": "강아지를 가족으로 맞이하고 싶어서",
      "가족 동의": "네",
      "반려동물 경험": "2년 동안 강아지 키운 경험 있음",
      "현재 반려동물 여부": "없음",
      "입양 후 돌봄 계획": "매일 산책, 건강검진 주기적 시행",
      "분양비 동의": "네",
      "중성화 동의": "네",
      "끝까지 책임 약속": "네"
    }
  });

  // if (loading || !data) return <div className="adoption-detail-loading">로딩 중...</div>;

  return (
    <div className="adoption-detail-container">
      <div className="adoption-detail-header">
        <span className={`adoption-detail-status adoption-status--${data.status?.toLowerCase()}`}>
          {statusToKor(data.status)}
        </span>
        <span className="adoption-detail-date">
          방문일: {data.visitDate} {data.visitTime}
        </span>
      </div>

      <div className="adoption-detail-main">
        {/* 강아지 정보 */}
        <div className="adoption-detail-dog">
          <div className="adoption-detail-dogname">{data.dogName}</div>
        </div>
        {/* 신청자 정보 */}
        <div className="adoption-detail-member">
          <div><b>신청자:</b> {data.memberName}</div>
          <div><b>연락처:</b> {data.memberPhone}</div>
        </div>
        {/* 신청 정보 */}
        <div className="adoption-detail-meta">
          <div><b>신청일:</b> {data.createdAt?.split("T")[0]}</div>
          {data.decisionAt && <div><b>승인/거절일:</b> {data.decisionAt?.split("T")[0]}</div>}
        </div>
        {/* 설문지 */}
        {data.survey && (
          <div className="adoption-detail-survey">
            <div className="adoption-survey-title">입양 설문 답변</div>
            {Object.entries(data.survey).map(([q, a]) => (
              <div key={q} className="adoption-survey-item">
                <span className="adoption-survey-q">{q}</span>
                <span className="adoption-survey-a">{a}</span>
              </div>
            ))}
          </div>
        )}
        {/* 버튼 */}
        <div className="adoption-detail-actions">
          {data.status === "PENDING" && (
            <>
              {/* <button className="adoption-approve-btn" onClick={handleApprove}>승인</button>
              <button className="adoption-reject-btn" onClick={handleReject}>거절</button> */}
              <button className="adoption-approve-btn" onClick={() => alert("승인 동작(더미)")}>승인</button>
              <button className="adoption-reject-btn" onClick={() => alert("거절 동작(더미)")}>거절</button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default AdoptionDetail;
