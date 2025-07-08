import { useState } from "react";
import '../../styles/shelter/donation/donationList.css';
import Pagination from "../../components/shelter/common/Pagination";

// 상태 한글 변환
function statusToKor(status) {
  switch (status) {
    case "SUCCESS": return "성공";
    case "PENDING": return "진행중";
    case "FAILED": return "실패";
    default: return status;
  }
}

// 결제 수단 한글 변환
function methodToKor(method) {
  switch (method) {
    case "CARD": return "카드결제";
    case "ACCOUNT": return "계좌이체";
    case "NAVER": return "네이버페이";
    default: return method;
  }
}

// ✅ 더미 후원 데이터 생성
function generateDummyDonations(count) {
  const names = ["홍길동", "김후원", "이영희", "박민수", "익명"];
  const statuses = ["SUCCESS", "PENDING", "FAILED"];
  const methods = ["CARD", "ACCOUNT", "NAVER"];
  const messages = ["작게나마 보탭니다.", "응원합니다!", "", "강아지들 건강하게!", ""];

  const list = [];
  for (let i = 1; i <= count; i++) {
    const name = names[i % names.length];
    const status = statuses[i % statuses.length];
    const method = methods[i % methods.length];
    const message = messages[i % messages.length];
    const amount = 10000 + (i % 5) * 10000;

    const date = new Date(2024, 5, 1 + (i % 15)); // 6월 1~15일
    const dateStr = date.toISOString().split("T")[0];
    const timeStr = `${9 + (i % 8)}:00:00`;

    list.push({
      id: i,
      orderName: name,
      amount: amount,
      createdAt: `${dateStr}T${timeStr}`,
      status,
      method,
      message,
    });
  }
  return list;
}

export default function DonationList() {
  const [donationsMonth] = useState({ amount: 120000 });
  const [donationsTotal] = useState({ amount: 32400000 });
  const [currentPage, setCurrentPage] = useState(1);
  const [donations] = useState(generateDummyDonations(150)); //

  const itemsPerPage = 10;
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentDonations = donations.slice(indexOfFirstItem, indexOfLastItem);

  return (
    <div className="donation-container">
      <h2 className="donation-title">후원 내역 관리</h2>
      <h3 className="donation-all">총 후원 금액 : {donationsTotal.amount.toLocaleString()}원</h3>
      <h3 className="donation-all">이번달 후원 금액 : {donationsMonth.amount.toLocaleString()}원</h3>

      <ul className="donation-list">
        {currentDonations.map(d => (
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

      {donations.length > itemsPerPage && (
        <Pagination
          totalItems={donations.length}
          itemPerPage={itemsPerPage}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      )}
    </div>
  );
}
