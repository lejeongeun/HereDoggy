import React from "react";
import styles from '../../../styles/admin/adminReport/report.module.css';

function ReportList({ reports, onSelect, requestSort, sortConfig }) {
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
            <th onClick={() => requestSort('type')}>유형{getSortIndicator('type')}</th>
            <th onClick={() => requestSort('target')}>신고 대상{getSortIndicator('target')}</th>
            <th onClick={() => requestSort('reporter')}>신고자{getSortIndicator('reporter')}</th>
            <th onClick={() => requestSort('status')}>상태{getSortIndicator('status')}</th>
            <th onClick={() => requestSort('date')}>신고일{getSortIndicator('date')}</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {reports.length === 0 ? (
            <tr>
              <td colSpan={7} style={{ textAlign: "center", color: "#aaa", padding: 28 }}>
                신고 내역이 없습니다.
              </td>
            </tr>
          ) : (
            reports.map((report) => (
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
    </div>
  );
}

export default ReportList;
