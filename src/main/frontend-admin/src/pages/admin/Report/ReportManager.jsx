import React, { useState, useMemo } from "react";
import ReportList from "./ReportList";
import ReportDetailModal from "./ReportDetailModal";
import styles from '../../../styles/admin/adminReport/report.module.css';
import commonStyles from '../../../styles/admin/common/adminControls.module.css'; // 공통 스타일 임포트
import AdminPagination from '../../../components/admin/common/AdminPagination';

// 더미 데이터 생성 함수
const generateDummyReports = (count) => {
  const dummyData = [];
  const types = ["게시글", "댓글", "보호소", "유저"];
  const statuses = ["미처리", "처리완료"];
  const reporters = ["user01", "user02", "user03", "admin01", "user04", "user05", "user06", "user07", "user08", "user09"];
  const targets = [
    "산책후기 제목입니다",
    "이 댓글 신고합니다",
    "행복보호소",
    "user04",
    "불법 광고 게시글",
    "욕설 댓글",
    "허위 정보 보호소",
    "부적절한 프로필 사진",
    "도배성 게시글",
    "사기성 유저",
  ];

  for (let i = 1; i <= count; i++) {
    const randomType = types[Math.floor(Math.random() * types.length)];
    const randomStatus = statuses[Math.floor(Math.random() * statuses.length)];
    const randomReporter = reporters[Math.floor(Math.random() * reporters.length)];
    const randomTarget = targets[Math.floor(Math.random() * targets.length)];
    const randomDay = Math.floor(Math.random() * 30) + 1;
    const randomMonth = Math.floor(Math.random() * 6) + 1; // 1월부터 6월까지
    const date = `2025-${String(randomMonth).padStart(2, '0')}-${String(randomDay).padStart(2, '0')}`;

    dummyData.push({
      id: i,
      type: randomType,
      target: randomTarget,
      reporter: randomReporter,
      status: randomStatus,
      date: date,
      content: `이것은 ${randomTarget}에 대한 ${randomType} 신고 내용입니다. 신고자: ${randomReporter}.`,
    });
  }
  return dummyData;
};

function ReportManager() {
  const [reports, setReports] = useState(generateDummyReports(100)); // 100개의 더미 데이터 생성
  const [selectedReport, setSelectedReport] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedType, setSelectedType] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: null, direction: null });

  // 페이지네이션 상태
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10; // 한 페이지당 10개 항목

  // 필터링 및 검색 로직
  const filteredReports = useMemo(() => {
    let sortableItems = [...reports];

    // 필터링
    const filtered = sortableItems.filter(report => {
      const matchesSearchTerm = report.target.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                report.reporter.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                report.content.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesType = selectedType === '' || report.type === selectedType;
      const matchesStatus = selectedStatus === '' || report.status === selectedStatus;
      return matchesSearchTerm && matchesType && matchesStatus;
    });

    // 정렬
    if (sortConfig.key !== null) {
      filtered.sort((a, b) => {
        if (a[sortConfig.key] < b[sortConfig.key]) {
          return sortConfig.direction === 'ascending' ? -1 : 1;
        }
        if (a[sortConfig.key] > b[sortConfig.key]) {
          return sortConfig.direction === 'ascending' ? 1 : -1;
        }
        return 0;
      });
    }
    return filtered;
  }, [reports, searchTerm, selectedType, selectedStatus, sortConfig]);

  // 현재 페이지에 해당하는 데이터 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentReports = filteredReports.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(filteredReports.length / itemsPerPage);

  const handleSelectReport = (report) => {
    setSelectedReport(report);
  };

  const handleCloseModal = () => {
    setSelectedReport(null);
  };

  const handleProcessReport = (reportId) => {
    setReports(prevReports =>
      prevReports.map(report =>
        report.id === reportId ? { ...report, status: "처리완료" } : report
      )
    );
    setSelectedReport(null);  // 모달 닫기
  };

  const handleAction = (reportId) => {
    setReports(prevReports => prevReports.filter((report) => report.id !== reportId));
    setSelectedReport(null);  // 모달 닫기
  };

  // 페이지 변경 핸들러
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // 검색어 변경 핸들러
  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
  };

  // 유형 선택 변경 핸들러
  const handleTypeChange = (e) => {
    setSelectedType(e.target.value);
    setCurrentPage(1); // 필터 변경 시 첫 페이지로 이동
  };

  // 상태 선택 변경 핸들러
  const handleStatusChange = (e) => {
    setSelectedStatus(e.target.value);
    setCurrentPage(1); // 필터 변경 시 첫 페이지로 이동
  };

  // 정렬 요청 핸들러
  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const typeOptions = ['', '게시글', '댓글', '보호소', '유저'];
  const statusOptions = ['', '미처리', '처리완료'];

  return (
    <div className={styles.managerContainer}>
      <h1 className={commonStyles.managerTitle}>신고 관리</h1>
      <div className={commonStyles.controlsContainer}>
        <input
          type="text"
          placeholder="대상, 신고자, 내용 검색"
          value={searchTerm}
          onChange={handleSearchChange}
          className={commonStyles.searchInput}
        />
        <select value={selectedType} onChange={handleTypeChange} className={commonStyles.selectInput}>
          {typeOptions.map(type => (
            <option key={type} value={type}>
              {type === '' ? '전체 유형' : type}
            </option>
          ))}
        </select>
        <select value={selectedStatus} onChange={handleStatusChange} className={commonStyles.selectInput}>
          {statusOptions.map(status => (
            <option key={status} value={status}>
              {status === '' ? '전체 상태' : status}
            </option>
          ))}
        </select>
      </div>
      <ReportList reports={currentReports} onSelect={handleSelectReport} requestSort={requestSort} sortConfig={sortConfig} />
      <AdminPagination
        totalItems={filteredReports.length}
        itemPerPage={itemsPerPage}
        currentPage={currentPage}
        onPageChange={handlePageChange}
      />
      {selectedReport && (
        <ReportDetailModal
          report={selectedReport}
          onClose={handleCloseModal}
          onProcess={handleProcessReport}
          onAction={handleAction}
        />
      )}
    </div>
  );
}

export default ReportManager;
