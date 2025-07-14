
import React, { useState, useMemo } from 'react';
import styles from '../../../styles/admin/partnership/partnership.module.css';
import commonStyles from '../../../styles/admin/common/adminControls.module.css'; // 공통 스타일 임포트
import PartnershipDetailModal from './PartnershipDetailModal';
import AdminPagination from '../../../components/admin/common/AdminPagination'; // Pagination 컴포넌트 임포트

// 더미 데이터 생성 함수
const generateDummyRequests = (count) => {
  const dummyData = [];
  const companyNames = ['행복한 펫푸드', '튼튼한 장난감', '댕댕이 월드', '멍냥 상점', '펫사랑 용품', '도그 워커스', '캣츠 앤 독스', '애니멀 케어', '프렌들리 펫', '글로벌 펫'];
  const productTypes = ['사료', '장난감', '의류', '간식', '미용용품', '훈련용품', '건강보조제', '이동장', '하우스', '목줄'];
  const statuses = ['pending', 'approved', 'rejected'];
  const regions = ['서울', '경기', '부산', '대구', '인천', '광주', '대전', '울산', '세종', '강원', '충북', '충남', '전북', '전남', '경북', '경남', '제주'];

  for (let i = 1; i <= count; i++) {
    const randomCompanyName = companyNames[Math.floor(Math.random() * companyNames.length)];
    const randomProductType = productTypes[Math.floor(Math.random() * productTypes.length)];
    const randomStatus = statuses[Math.floor(Math.random() * statuses.length)];
    const randomDay = Math.floor(Math.random() * 30) + 1;
    const randomMonth = Math.floor(Math.random() * 7) + 1; // 1월부터 7월까지
    const requestDate = `2025-${String(randomMonth).padStart(2, '0')}-${String(randomDay).padStart(2, '0')}`;
    const randomRegion = regions[Math.floor(Math.random() * regions.length)];

    dummyData.push({
      id: i,
      companyName: `${randomCompanyName} ${i}`,
      productType: randomProductType,
      businessRegistrationNumber: `123-45-${String(10000 + i).padStart(5, '0')}`,
      status: randomStatus,
      requestDate: requestDate,
      region: randomRegion,
    });
  }
  return dummyData;
};

const PartnershipManager = () => {
  const [requests, setRequests] = useState(generateDummyRequests(50)); // 50개의 더미 데이터 생성
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState(''); // 검색어 상태
  const [selectedRegion, setSelectedRegion] = useState(''); // 선택된 지역 상태

  // 페이지네이션 상태
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10; // 한 페이지당 10개 항목

  // 필터링 및 검색 로직
  const filteredRequests = useMemo(() => {
    return requests.filter(request => {
      const matchesSearchTerm = request.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                request.productType.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                request.businessRegistrationNumber.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesRegion = selectedRegion === '' || request.region === selectedRegion;
      return matchesSearchTerm && matchesRegion;
    });
  }, [requests, searchTerm, selectedRegion]);

  // 현재 페이지에 해당하는 데이터 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentRequests = filteredRequests.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(filteredRequests.length / itemsPerPage);

  const handleOpenModal = (request) => {
    setSelectedRequest(request);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedRequest(null);
    setIsModalOpen(false);
  };

  const handleUpdateRequest = (updatedRequest) => {
    setRequests(requests.map(req => req.id === updatedRequest.id ? updatedRequest : req));
    handleCloseModal();
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'pending':
        return '대기중';
      case 'approved':
        return '승인됨';
      case 'rejected':
        return '거절됨';
      default:
        return '';
    }
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

  // 지역 선택 변경 핸들러
  const handleRegionChange = (e) => {
    setSelectedRegion(e.target.value);
    setCurrentPage(1); // 지역 변경 시 첫 페이지로 이동
  };

  const regions = ['', '서울', '경기', '부산', '대구', '인천', '광주', '대전', '울산', '세종', '강원', '충북', '충남', '전북', '전남', '경북', '경남', '제주'];

  return (
    <div className={styles['partnership-container']}>
      <h1 className={commonStyles.managerTitle}>협약 업체 관리</h1>
      <div className={commonStyles.controlsContainer}>
        <input
          type="text"
          placeholder="업체명, 제품 유형, 사업자등록번호 검색"
          value={searchTerm}
          onChange={handleSearchChange}
          className={commonStyles.searchInput}
        />
        <select
          value={selectedRegion}
          onChange={handleRegionChange}
          className={commonStyles.selectInput}
        >
          {regions.map(region => (
            <option key={region} value={region}>
              {region === '' ? '전체 지역' : region}
            </option>
          ))}
        </select>
      </div>
      <table className={styles['partnership-table']}>
        <thead>
          <tr>
            <th>요청 ID</th>
            <th>업체명</th>
            <th>제품 유형</th>
            <th>사업자등록번호</th>
            <th>요청일</th>
            <th>지역</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          {currentRequests.map((request) => ( // currentRequests 사용
            <tr key={request.id} onClick={() => handleOpenModal(request)} className={styles['partnership-tableRow']}>
              <td>{request.id}</td>
              <td>{request.companyName}</td>
              <td>{request.productType}</td>
              <td>{request.businessRegistrationNumber}</td>
              <td>{request.requestDate}</td>
              <td>{request.region}</td>
              <td>
                <span className={styles[`partnership-${request.status}`]}>{getStatusText(request.status)}</span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <AdminPagination
        totalItems={filteredRequests.length}
        itemPerPage={itemsPerPage}
        currentPage={currentPage}
        onPageChange={handlePageChange}
      />
      {isModalOpen && (
        <PartnershipDetailModal
          request={selectedRequest}
          onClose={handleCloseModal}
          onUpdate={handleUpdateRequest}
        />
      )}
    </div>
  );
};

export default PartnershipManager;
