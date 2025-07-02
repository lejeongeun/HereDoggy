import React, { useState } from "react";
import InquiryList from "./InquiryList";
import InquiryDetailModal from "./InquiryDetailModal";
import '../../../styles/admin/inquiry/inquiry.css';

const dummyInquiries = [
  {
    id: 1,
    user: "user01",
    subject: "산책 예약 방법",
    status: "처리중",
    date: "2025-06-30",
  },
  {
    id: 2,
    user: "user02",
    subject: "입양 관련 문의",
    status: "답변완료",
    date: "2025-06-28",
  },
  {
    id: 3,
    user: "user03",
    subject: "서비스 오류 신고",
    status: "처리중",
    date: "2025-06-29",
  },
];

function InquiryManager() {
  const [selectedInquiry, setSelectedInquiry] = useState(null);

  const handleSelectInquiry = (inquiry) => {
    setSelectedInquiry(inquiry);  // 상세보기 클릭 시 해당 문의를 선택
  };

  const handleCloseModal = () => {
    setSelectedInquiry(null);  // 모달 닫기
  };

  const handleProcessInquiry = (inquiryId, status, response) => {
    console.log("상태 변경:", inquiryId, status, response);
    // TODO: 상태 변경 및 답변 제출 처리 로직
    // 예시: API 호출로 상태 업데이트 및 답변 저장
    setSelectedInquiry(null);  // 처리 후 모달 닫기
  };

  return (
    <div>
      <InquiryList inquiries={dummyInquiries} onSelect={handleSelectInquiry} />
      <InquiryDetailModal
        inquiry={selectedInquiry}
        onClose={handleCloseModal}
        onProcess={handleProcessInquiry}
      />
    </div>
  );
}

export default InquiryManager;
