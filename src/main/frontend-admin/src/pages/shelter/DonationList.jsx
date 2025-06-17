import { useState } from "react";
import '../../styles/shelter/donation/donationList.css';


function statusToKor(status) {
  switch (status) {
    case "SUCCESS": return "성공";
    case "PENDING": return "진행중";
    case "FAILED": return "실패";
    default: return status;
  }
}

function methodToKor(method) {
  switch (method) {
    case "CARD": return "카드결제";
    case "ACCOUNT": return "계좌이체";
    case "NAVER": return "네이버페이";
    default: return method;
  }
}

export default function DonationList() {
  // 더미데이터
  const [donations] = useState([
    {
      id: 1,
      orderName: "홍길동",
      amount: 50000,
      createdAt: "2024-06-11T16:18:00",
      status: "SUCCESS",
      method: "CARD",
      message: "강아지들 건강하게!",
    },
    {
      id: 2,
      orderName: "김후원",
      amount: 30000,
      createdAt: "2024-06-09T10:22:00",
      status: "SUCCESS",
      method: "NAVER",
      message: "",
    },
    {
      id: 3,
      orderName: "이영희",
      amount: 20000,
      createdAt: "2024-06-05T12:50:00",
      status: "FAILED",
      method: "ACCOUNT",
      message: "작게나마 보탭니다.",
    },
    {
      id: 4,
      orderName: "익명",
      amount: 10000,
      createdAt: "2024-06-03T14:00:00",
      status: "PENDING",
      method: "CARD",
      message: "",
    },
  ]);

  return (
    <div className="donation-container">
      <h2 className="donation-title">후원 내역 관리</h2>
      <ul className="donation-list">
        {donations.map(d => (
          <li key={d.id} className="donation-item">
            <div className="donation-info-row">
              <span className="donation-name">{d.orderName}</span>
              <span className="donation-dot">·</span>
              <span className="donation-amount">{d.amount.toLocaleString()}원</span>
              <span className="donation-dot">·</span>
              <span className={`donation-status donation-status--${d.status.toLowerCase()}`}>{statusToKor(d.status)}</span>
              <span className="donation-dot">·</span>
              <span className="donation-method">{methodToKor(d.method)}</span>
              <span className="donation-dot">·</span>
              <span className="donation-date">{d.createdAt.split("T")[0]}</span>
            </div>
            {d.message && (
              <div className="donation-message">"{d.message}"</div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
