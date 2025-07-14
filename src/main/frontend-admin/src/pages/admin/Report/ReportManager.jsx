import React, { useState } from "react";
import ReportList from "./ReportList";
import ReportDetailModal from "./ReportDetailModal";
import styles from '../../../styles/admin/adminReport/report.module.css';

const dummyReports = [
  {
    id: 1,
    type: "게시글",
    target: "산책후기 제목입니다",
    reporter: "user01",
    status: "미처리",
    date: "2025-06-30",
  },
  {
    id: 2,
    type: "댓글",
    target: "이 댓글 신고합니다",
    reporter: "user02",
    status: "처리완료",
    date: "2025-06-28",
  },
  {
    id: 3,
    type: "보호소",
    target: "행복보호소",
    reporter: "user03",
    status: "미처리",
    date: "2025-06-29",
  },
  {
    id: 4,
    type: "유저",
    target: "user04",
    reporter: "user05",
    status: "미처리",
    date: "2025-06-27",
  }
];

function ReportManager() {
  const [reports, setReports] = useState(dummyReports);
  const [selectedReport, setSelectedReport] = useState(null);

  const handleSelectReport = (report) => {
    setSelectedReport(report);
  };

  const handleCloseModal = () => {
    setSelectedReport(null);
  };

  const handleProcessReport = (reportId) => {
    console.log("처리 완료:", reportId);
    // 상태를 처리 완료로 업데이트
    const updatedReports = reports.map((report) => 
      report.id === reportId ? { ...report, status: "처리완료" } : report
    );
    setReports(updatedReports);
    setSelectedReport(null);  // 모달 닫기
  };

  const handleAction = (reportId) => {
    console.log("삭제 / 비활성화:", reportId);
    // 해당 신고 삭제 또는 비활성화 처리
    const updatedReports = reports.filter((report) => report.id !== reportId);
    setReports(updatedReports);
    setSelectedReport(null);  // 모달 닫기
  };

  return (
    <div className={styles.managerContainer}>
      <ReportList reports={reports} onSelect={handleSelectReport} />
      <ReportDetailModal
        report={selectedReport}
        onClose={handleCloseModal}
        onProcess={handleProcessReport}
        onAction={handleAction}
      />
    </div>
  );
}

export default ReportManager;
