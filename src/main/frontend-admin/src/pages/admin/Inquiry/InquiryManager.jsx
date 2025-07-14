import React, { useState, useMemo } from "react";
import InquiryList from "./InquiryList";
import InquiryDetailModal from "./InquiryDetailModal";
import styles from '../../../styles/admin/inquiry/inquiry.module.css';
import commonStyles from '../../../styles/admin/common/adminControls.module.css'; // 공통 스타일 임포트
import AdminPagination from '../../../components/admin/common/AdminPagination';

// 더미 데이터 생성 함수
const generateDummyInquiries = (count) => {
  const dummyData = [];
  const users = ["user01", "user02", "user03", "admin01", "user04", "user05", "user06", "user07", "user08", "user09"];
  const subjects = [
    "산책 예약 방법 문의",
    "입양 절차 문의",
    "서비스 오류 신고 (로그인)",
    "보호소 정보 수정 요청",
    "기부금 영수증 발급 문의",
    "강아지 등록 관련 문의",
    "사이트 이용 불편 사항",
    "파트너십 제안",
    "개인정보 변경 문의",
    "기타 문의 사항",
  ];
  const statuses = ["처리중", "답변완료", "대기중"];
  const types = ["일반", "기술", "결제", "제안", "기타"];

  for (let i = 1; i <= count; i++) {
    const randomUser = users[Math.floor(Math.random() * users.length)];
    const randomSubject = subjects[Math.floor(Math.random() * subjects.length)];
    const randomStatus = statuses[Math.floor(Math.random() * statuses.length)];
    const randomType = types[Math.floor(Math.random() * types.length)];
    const randomDay = Math.floor(Math.random() * 30) + 1;
    const randomMonth = Math.floor(Math.random() * 6) + 1; // 1월부터 6월까지
    const date = `2025-${String(randomMonth).padStart(2, '0')}-${String(randomDay).padStart(2, '0')}`;

    dummyData.push({
      id: i,
      user: randomUser,
      subject: randomSubject,
      status: randomStatus,
      date: date,
      type: randomType,
      content: `이것은 ${randomSubject}에 대한 상세 문의 내용입니다. 사용자 ${randomUser}가 ${date}에 문의했습니다.`, // 상세 내용 추가
    });
  }
  return dummyData;
};

function InquiryManager() {
  const [inquiries, setInquiries] = useState(generateDummyInquiries(100)); // 100개의 더미 데이터 생성
  const [selectedInquiry, setSelectedInquiry] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');
  const [selectedType, setSelectedType] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: null, direction: null });

  // 페이지네이션 상태
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10; // 한 페이지당 10개 항목

  // 필터링 및 검색 로직
  const filteredInquiries = useMemo(() => {
    let sortableItems = [...inquiries];

    // 필터링
    const filtered = sortableItems.filter(inquiry => {
      const matchesSearchTerm = inquiry.user.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                inquiry.subject.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                inquiry.content.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesStatus = selectedStatus === '' || inquiry.status === selectedStatus;
      const matchesType = selectedType === '' || inquiry.type === selectedType;
      return matchesSearchTerm && matchesStatus && matchesType;
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
  }, [inquiries, searchTerm, selectedStatus, selectedType, sortConfig]);

  // 현재 페이지에 해당하는 데이터 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentInquiries = filteredInquiries.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(filteredInquiries.length / itemsPerPage);

  const handleSelectInquiry = (inquiry) => {
    setSelectedInquiry(inquiry);
  };

  const handleCloseModal = () => {
    setSelectedInquiry(null);
  };

  const handleProcessInquiry = (inquiryId, status, response) => {
    setInquiries(prevInquiries =>
      prevInquiries.map(inquiry =>
        inquiry.id === inquiryId ? { ...inquiry, status: status, response: response } : inquiry
      )
    );
    setSelectedInquiry(null);
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

  // 상태 선택 변경 핸들러
  const handleStatusChange = (e) => {
    setSelectedStatus(e.target.value);
    setCurrentPage(1); // 필터 변경 시 첫 페이지로 이동
  };

  // 유형 선택 변경 핸들러
  const handleTypeChange = (e) => {
    setSelectedType(e.target.value);
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

  const statusOptions = ['', '처리중', '답변완료', '대기중'];
  const typeOptions = ['', '일반', '기술', '결제', '제안', '기타'];

  return (
    <div className={styles.managerContainer}>
      <h1 className={commonStyles.managerTitle}>문의 관리</h1>
      <div className={commonStyles.controlsContainer}>
        <input
          type="text"
          placeholder="사용자, 제목, 내용 검색"
          value={searchTerm}
          onChange={handleSearchChange}
          className={commonStyles.searchInput}
        />
        <select value={selectedStatus} onChange={handleStatusChange} className={commonStyles.selectInput}>
          {statusOptions.map(status => (
            <option key={status} value={status}>
              {status === '' ? '전체 상태' : status}
            </option>
          ))}
        </select>
        <select value={selectedType} onChange={handleTypeChange} className={commonStyles.selectInput}>
          {typeOptions.map(type => (
            <option key={type} value={type}>
              {type === '' ? '전체 유형' : type}
            </option>
          ))}
        </select>
      </div>
      <InquiryList inquiries={currentInquiries} onSelect={handleSelectInquiry} requestSort={requestSort} sortConfig={sortConfig} />
      <AdminPagination
        totalItems={filteredInquiries.length}
        itemPerPage={itemsPerPage}
        currentPage={currentPage}
        onPageChange={handlePageChange}
      />
      {selectedInquiry && (
        <InquiryDetailModal
          inquiry={selectedInquiry}
          onClose={handleCloseModal}
          onProcess={handleProcessInquiry}
        />
      )}
    </div>
  );
}

export default InquiryManager;
