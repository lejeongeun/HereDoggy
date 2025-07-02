import React, { useState } from "react";
import '../../../styles/admin/inquiry/inquiry.css';

function InquiryDetailModal({ inquiry, onClose, onProcess }) {
  const [response, setResponse] = useState("");
  const [status, setStatus] = useState(inquiry?.status || "처리중");

  const handleResponseChange = (e) => {
    setResponse(e.target.value);
  };


  const handleSubmit = () => {
    // 답변 저장 또는 상태 변경 처리
    onProcess(inquiry.id, status, response);
    onClose();
  };

  if (!inquiry) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>문의 상세</h3>
        <p><strong>ID:</strong> {inquiry.id}</p>
        <p><strong>사용자:</strong> {inquiry.user}</p>
        <p><strong>제목:</strong> {inquiry.subject}</p>
        <p><strong>내용:</strong> {inquiry.subject}</p>
        <p><strong>날짜:</strong> {inquiry.date}</p>

        <div className="modal-actions">

          <div>
            <label>답변</label>
            <textarea 
              value={response}
              onChange={handleResponseChange}
              placeholder="문의에 대한 답변을 작성하세요"
            />
          </div>

          <button className="action-btn process" onClick={handleSubmit}>답변 제출</button>
          <button className="action-btn close" onClick={onClose}>닫기</button>
        </div>
      </div>
    </div>
  );
}

export default InquiryDetailModal;
