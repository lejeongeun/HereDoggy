import React, { useState } from "react";
import styles from '../../../styles/admin/inquiry/inquiry.module.css';

function InquiryList({ inquiries, onSelect }) {
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10); // 한 페이지당 보여줄 항목 수

  // 현재 페이지의 항목 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = inquiries.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(inquiries.length / itemsPerPage);

  // 페이지 변경 핸들러
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className={styles.managerSection}>
      <h2 className={styles.managerHeader}>문의 목록</h2>
      <table className={styles.managerTable}>
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
          {currentItems.length === 0 ? (
            <tr>
              <td colSpan={6} style={{ textAlign: "center", color: "#aaa", padding: 28 }}>
                문의 내역이 없습니다.
              </td>
            </tr>
          ) : (
            currentItems.map((inquiry) => (
              <tr key={inquiry.id}>
                <td>{inquiry.id}</td>
                <td>{inquiry.user}</td>
                <td>{inquiry.subject}</td>
                <td>
                  <span className={`${styles.statusBadge} ${inquiry.status === "처리중" ? styles.pending : styles.done}`}>
                    {inquiry.status}
                  </span>
                </td>
                <td>{inquiry.date}</td>
                <td>
                  <button className={`${styles.actionBtn} ${styles.detailBtn}`} onClick={() => onSelect(inquiry)}>상세</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* Pagination Controls */}
      <div className={styles.pagination}>
        <button
          onClick={() => paginate(currentPage - 1)}
          disabled={currentPage === 1}
          className={styles.paginationButton}
        >
          이전
        </button>
        {[...Array(totalPages).keys()].map((number) => (
          <button
            key={number + 1}
            onClick={() => paginate(number + 1)}
            className={`${styles.paginationButton} ${currentPage === number + 1 ? styles.activePage : ''}`}
          >
            {number + 1}
          </button>
        ))}
        <button
          onClick={() => paginate(currentPage + 1)}
          disabled={currentPage === totalPages}
          className={styles.paginationButton}
        >
          다음
        </button>
      </div>
    </div>
  );
}

export default InquiryList;