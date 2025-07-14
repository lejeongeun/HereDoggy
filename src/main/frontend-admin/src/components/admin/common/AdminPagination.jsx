import React from 'react';
import styles from '../../../styles/admin/common/adminPagination.module.css';

export default function AdminPagination({
  totalItems,
  itemPerPage,
  currentPage,
  onPageChange,
}) {
  const totalPages = Math.ceil(totalItems / itemPerPage);
  let pageNumbers = [];

  if (totalPages <= 5) {
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }
  } else {
    pageNumbers.push(1);

    if (currentPage > 3) {
      pageNumbers.push("...");
    }

    const start = Math.max(2, currentPage - 1);
    const end = Math.min(totalPages - 1, currentPage + 1);

    for (let i = start; i <= end; i++) {
      pageNumbers.push(i);
    }

    if (currentPage < totalPages - 2) {
      pageNumbers.push("...");
    }

    pageNumbers.push(totalPages);
  }

  return (
    <div className={styles.pagination}>
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 1}
        className={styles.paginationButton}
      >
        이전
      </button>

      {pageNumbers.map((num, index) =>
        num === "..." ? (
          <span key={`ellipsis-${index}`} className={styles.paginationEllipsis}>...</span>
        ) : (
          <button
            key={num}
            onClick={() => onPageChange(num)}
            className={`${styles.paginationButton} ${currentPage === num ? styles.activePage : ""}`}
          >
            {num}
          </button>
        )
      )}

      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage === totalPages}
        className={styles.paginationButton}
      >
        다음
      </button>
    </div>
  );
}
