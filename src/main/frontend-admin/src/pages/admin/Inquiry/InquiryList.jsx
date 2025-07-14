import React from "react";
import styles from '../../../styles/admin/inquiry/inquiry.module.css';

function InquiryList({ inquiries, onSelect, requestSort, sortConfig }) {
  // 정렬 방향 표시
  const getSortIndicator = (key) => {
    if (sortConfig.key === key) {
      return sortConfig.direction === 'ascending' ? ' 🔼' : ' 🔽';
    }
    return '';
  };

  return (
    <div className={styles.managerSection}>
      <table className={styles.managerTable}>
        <thead>
          <tr>
            <th onClick={() => requestSort('id')}>ID{getSortIndicator('id')}</th>
            <th onClick={() => requestSort('user')}>사용자{getSortIndicator('user')}</th>
            <th onClick={() => requestSort('subject')}>제목{getSortIndicator('subject')}</th>
            <th onClick={() => requestSort('type')}>유형{getSortIndicator('type')}</th>
            <th onClick={() => requestSort('status')}>상태{getSortIndicator('status')}</th>
            <th onClick={() => requestSort('date')}>날짜{getSortIndicator('date')}</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {inquiries.length === 0 ? (
            <tr>
              <td colSpan={7} style={{ textAlign: "center", color: "#aaa", padding: 28 }}>
                문의 내역이 없습니다.
              </td>
            </tr>
          ) : (
            inquiries.map((inquiry) => (
              <tr key={inquiry.id}>
                <td>{inquiry.id}</td>
                <td>{inquiry.user}</td>
                <td>{inquiry.subject}</td>
                <td>{inquiry.type}</td>
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
    </div>
  );
}

export default InquiryList;