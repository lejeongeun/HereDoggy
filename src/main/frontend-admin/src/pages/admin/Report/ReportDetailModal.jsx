import React from "react";
import styles from '../../../styles/admin/adminReport/report.module.css';

function ReportDetailModal({ report, onClose, onProcess, onAction }) {
  if (!report) return null;

  const handleAction = () => {
    onAction(report.id); // 신고 대상에 대한 처리 (삭제, 비활성화 등)
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <h3 className={styles.modalHeader}>신고 상세</h3>
        <div className={styles.modalBody}>
          <p><strong>ID:</strong> {report.id}</p>
          <p><strong>유형:</strong> {report.type}</p>
          <p><strong>신고자:</strong> {report.reporter}</p>
          <p><strong>대상:</strong> {report.target}</p>
          <p><strong>신고일:</strong> {report.date}</p>
        </div>

        {/* 상태 변경 및 처리 */}
        <div className={styles.modalActions}>
          {report.status === "미처리" ? (
            <>
              <button className={`${styles.actionBtn} ${styles.processBtn}`} onClick={() => onProcess(report.id)}>처리 완료</button>
              <button className={`${styles.actionBtn} ${styles.deleteBtn}`} onClick={handleAction}>삭제 / 비활성화</button>
            </>
          ) : (
            <span className={styles.doneText}>조치 완료됨</span>
          )}
          <button className={`${styles.actionBtn} ${styles.closeBtn}`} onClick={onClose}>닫기</button>
        </div>
      </div>
    </div>
  );
}

export default ReportDetailModal;
