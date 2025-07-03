import React from "react";
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

function InquiryList({ onSelect }) {
  return (
    <div className="shelter-admin-wrap">
     <h2>문의 목록</h2>
    <table className="shelter-admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>사용자</th>
            <th>제목</th>
            <th>상태</th>
            <th>날짜</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {dummyInquiries.map((inquiry) => (
            <tr key={inquiry.id}>
              <td>{inquiry.id}</td>
              <td>{inquiry.user}</td>
              <td>{inquiry.subject}</td>
              <td>{inquiry.status}</td>
              <td>{inquiry.date}</td>
              <td>
                <button className="inquiry-detail-btn" onClick={() => onSelect(inquiry)}>상세</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default InquiryList;
