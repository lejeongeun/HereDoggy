import React, { useState } from "react";
import styles from '../../../styles/admin/adminReport/report.module.css';

function ReportList({ reports, onSelect }) {
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10); // 한 페이지당 보여줄 항목 수

  // 현재 페이지의 항목 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = reports.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(reports.length / itemsPerPage);

  // 페이지 변경 핸들러
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className={styles.managerSection}>
      <h2 className={styles.managerHeader}>신고 목록</h2>
      <table className={styles.managerTable}>
        <thead>
          <tr>
            <th>ID</th>
            <th>유형</th>
            <th>신고 대상</th>
            <th>신고자</th>
            <th>상태</th>
            <th>신고일</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {currentItems.length === 0 ? (
            <tr>
              <td colSpan={7} style={{ textAlign: "center", color: "#aaa", padding: 28 }}>
                신고 내역이 없습니다.
              </td>
            </tr>
          ) : (
            currentItems.map((report) => (
              <tr key={report.id}>
                <td>{report.id}</td>
                <td>{report.type}</td>
                <td>{report.target}</td>
                <td>{report.reporter}</td>
                <td>
                  <span className={`${styles.statusBadge} ${report.status === "미처리" ? styles.pending : styles.done}`}>
                    {report.status}
                  </span>
                </td>
                <td>{report.date}</td>
                <td>
                  <button className={`${styles.actionBtn} ${styles.detailBtn}`} onClick={() => onSelect(report)}>
                    상세
                  </button>
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

export default ReportList;
