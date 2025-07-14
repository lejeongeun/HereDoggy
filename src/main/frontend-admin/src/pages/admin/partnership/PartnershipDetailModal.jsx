
import React, { useState } from 'react';
import styles from '../../../styles/admin/partnership/partnership.module.css';

const PartnershipDetailModal = ({ request, onClose, onUpdate }) => {
  const [selectedShelterName, setSelectedShelterName] = useState('');
  const [shelterSearchTerm, setShelterSearchTerm] = useState('');
  const [showShelterDropdown, setShowShelterDropdown] = useState(false);

  // Placeholder for shelter list (will be fetched from API later)
  const allShelters = [
    { id: 1, name: '강남 보호소' },
    { id: 2, name: '강북 보호소' },
    { id: 3, name: '서초 보호소' },
    { id: 4, name: '송파 보호소' },
    { id: 5, name: '용산 보호소' },
    { id: 6, name: '종로 보호소' },
    { id: 7, name: '마포 보호소' },
    { id: 8, name: '영등포 보호소' },
    { id: 9, name: '구로 보호소' },
    { id: 10, name: '동대문 보호소' },
    { id: 11, name: '성동 보호소' },
    { id: 12, name: '광진 보호소' },
    { id: 13, name: '도봉 보호소' },
    { id: 14, name: '노원 보호소' },
    { id: 15, name: '은평 보호소' },
    { id: 16, name: '서대문 보호소' },
    { id: 17, name: '중랑 보호소' },
    { id: 18, name: '강동 보호소' },
    { id: 19, name: '강서 보호소' },
    { id: 20, name: '양천 보호소' },
  ];

  const filteredShelters = allShelters.filter(shelter =>
    shelter.name.toLowerCase().includes(shelterSearchTerm.toLowerCase())
  );

  const handleShelterSelect = (shelter) => {
    setSelectedShelterName(shelter.name);
    setShelterSearchTerm(shelter.name);
    setShowShelterDropdown(false);
  };

  const handleApprove = () => {
    if (!selectedShelterName) {
      alert('매칭할 보호소를 선택해주세요.');
      return;
    }
    onUpdate({ ...request, status: 'approved', matchedShelter: selectedShelterName });
  };

  const handleReject = () => {
    onUpdate({ ...request, status: 'rejected' });
  };

  return (
    <div className={styles['partnership-modalOverlay']}>
      <div className={styles['partnership-modalContent']}>
        <button className={styles['partnership-modalCloseIcon']} onClick={onClose}>&times;</button>
        <h2 className={styles['partnership-modalTitle']}>협약 요청 상세</h2>
        <div className={styles['partnership-modalSection']}>
          <p><strong>요청 ID:</strong> {request.id}</p>
          <p><strong>업체명:</strong> {request.companyName}</p>
          <p><strong>제품 유형:</strong> {request.productType}</p>
          <p><strong>사업자등록번호:</strong> {request.businessRegistrationNumber}</p>
          <p><strong>요청일:</strong> {request.requestDate}</p>
          <p><strong>상태:</strong> {request.status}</p>
        </div>
        
        {request.status === 'pending' && (
          <div className={styles['partnership-modalSection']}>
            <h3 className={styles['partnership-shelterSelectionTitle']}>보호소 매칭</h3>
            <div className={styles['partnership-shelterSearchContainer']}>
              <input
                type="text"
                className={styles['partnership-shelterSearchInput']}
                placeholder="보호소 검색..."
                value={shelterSearchTerm}
                onChange={(e) => {
                  setShelterSearchTerm(e.target.value);
                  setShowShelterDropdown(true);
                }}
                onFocus={() => setShowShelterDropdown(true)}
                onBlur={() => setTimeout(() => setShowShelterDropdown(false), 100)} // Delay to allow click on dropdown item
              />
              {showShelterDropdown && filteredShelters.length > 0 && (
                <ul className={styles['partnership-shelterDropdown']}>
                  {filteredShelters.map(shelter => (
                    <li key={shelter.id} onMouseDown={() => handleShelterSelect(shelter)}>
                      {shelter.name}
                    </li>
                  ))}
                </ul>
              )}
              {showShelterDropdown && filteredShelters.length === 0 && shelterSearchTerm && (
                <div className={styles['partnership-noResults']}>검색 결과가 없습니다.</div>
              )}
            </div>
          </div>
        )}

        <div className={styles['partnership-modalActions']}>
          {request.status === 'pending' && (
            <>
              <button onClick={handleApprove} className={`${styles['partnership-button']} ${styles['partnership-approveButton']}`}>승인</button>
              <button onClick={handleReject} className={`${styles['partnership-button']} ${styles['partnership-rejectButton']}`}>거절</button>
            </>
          )}
          <button onClick={onClose} className={`${styles['partnership-button']} ${styles['partnership-closeButton']}`}>닫기</button>
        </div>
      </div>
    </div>
  );
};

export default PartnershipDetailModal;
