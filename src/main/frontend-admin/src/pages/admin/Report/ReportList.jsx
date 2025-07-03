import React from "react";
import '../../../styles/admin/adminReport/report.css';

function ReportList({ reports, onSelect }) {
  return (
    <div className="shelter-admin-wrap">
     <h2>신고 목록</h2>
    <table className="shelter-admin-table">
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
          {/* dummyReports 배열을 map() 함수로 순회 */}
          {reports.map((report) => (
            <tr key={report.id}>
              <td>{report.id}</td>
              <td>{report.type}</td>
              <td>{report.target}</td>
              <td>{report.reporter}</td>
              <td>
                <span className={`status-badge ${report.status === "미처리" ? "pending" : "done"}`}>
                  {report.status}
                </span>
              </td>
              <td>{report.date}</td>
              <button className="admin-btn-dark" onClick={() => onSelect(report)}>
                상세
              </button>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ReportList;
