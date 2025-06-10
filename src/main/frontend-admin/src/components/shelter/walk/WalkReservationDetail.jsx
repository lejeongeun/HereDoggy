import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getReservationDetail, approveReservation, rejectReservation } from "../../../api/shelter/reservation";

function WalkReservationDetail() {
  const { id } = useParams();
  const sheltersId = localStorage.getItem("shelters_id");
  const [data, setData] = useState(null);

  const fetchDetail = async () => {
    const res = await getReservationDetail(sheltersId, id);
    setData(res.data);
  };

  useEffect(() => {
    fetchDetail();
  }, []);

  const handleApprove = async () => {
    await approveReservation(sheltersId, id);
    alert("승인 완료");
    fetchDetail();
  };

  const handleReject = async () => {
    await rejectReservation(sheltersId, id);
    alert("거절 완료");
    fetchDetail();
  };

  if (!data) return <p>로딩 중...</p>;

  return (
    <div>
      <h2>예약 상세</h2>
      <p><strong>예약자:</strong> {data.memberName} ({data.memberEmail}, {data.memberPhone})</p>
      <p><strong>강아지:</strong> {data.dogName} [{data.dogStatus}]</p>
      <p><strong>일정:</strong> {data.date} {data.startTime}~{data.endTime}</p>
      <p><strong>요청사항:</strong> {data.note || "없음"}</p>
      <p><strong>신청일시:</strong> {new Date(data.createAt).toLocaleString()}</p>
      <p><strong>상태:</strong> {data.walkReservationStatus}</p>
      {data.walkReservationStatus === "PENDING" && (
        <>
          <button onClick={handleApprove}>승인</button>
          <button onClick={handleReject}>거절</button>
        </>
      )}
    </div>
  );
}

export default WalkReservationDetail;
