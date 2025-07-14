import React, { useState, useEffect } from 'react';
import PartnerCard from '../../components/shelter/partner/PartnerCard';
import '../../styles/shelter/partnerRequestList/partnerRequestList.css';

// API 응답을 시뮬레이션하기 위한 더미 데이터
const dummyPartnerMatches = [
    {
        id: 1,
        companyName: '펫케어',
        productType: '사료',
        businessNumber: '123-45-67890',
        description: '친환경 프리미엄 사료 전문 기업',
        // 'AWAITING_SHELTER_APPROVAL'는 보호소의 승인을 기다리는 상태
        status: 'AWAITING_SHELTER_APPROVAL',
    },
    {
        id: 2,
        companyName: '멍멍이장난감',
        productType: '장난감',
        businessNumber: '123-45-67891',
        description: '반려견 전용 장난감 제조사',
        status: 'AWAITING_SHELTER_APPROVAL',
    },
    {
        id: 3,
        companyName: '해피펫',
        productType: '간식',
        businessNumber: '111-22-33333',
        description: '수제 간식 전문 업체',
        // 'APPROVED'는 보호소가 최종 승인한 상태
        status: 'APPROVED',
    },
      {
        id: 4,
        companyName: '애니멀 헬스케어',
        productType: '의약품',
        businessNumber: '444-55-66666',
        description: '동물용 의약품 공급',
        // 'REJECTED'는 보호소가 거절한 상태
        status: 'REJECTED',
    },
];

const PartnerRequestList = () => {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('pending'); // 'pending' 또는 'completed'

  useEffect(() => {
    const fetchMatches = async () => {
      try {
        // 실제 API 호출 시, 현재 보호소 ID를 기반으로 매칭된 업체 목록을 가져옵니다.
        // 예: /api/shelters/{shelterId}/partner-matches
        await new Promise(resolve => setTimeout(resolve, 500)); // 로딩 시뮬레이션
        setMatches(dummyPartnerMatches);
        setLoading(false);
      } catch (err) {
        setError('데이터를 불러오는 데 실패했습니다.');
        setLoading(false);
      }
    };

    fetchMatches();
  }, []);

  const handleApprove = (id) => {
    setMatches(prev =>
      prev.map(p =>
        p.id === id ? { ...p, status: 'APPROVED' } : p
      )
    );
  };

  const handleReject = (id) => {
    setMatches(prev =>
      prev.map(p =>
        p.id === id ? { ...p, status: 'REJECTED' } : p
      )
    );
  };

  if (loading) {
    return <div className="partner-container"><h2>로딩 중...</h2></div>;
  }

  if (error) {
    return <div className="partner-container"><h2>{error}</h2></div>;
  }

  const pendingMatches = matches.filter(m => m.status === 'AWAITING_SHELTER_APPROVAL');
  const completedMatches = matches.filter(m => m.status === 'APPROVED' || m.status === 'REJECTED');

  return (
    <div className="partner-container">
      <h2 className="partner-title">협약 업체 관리</h2>
      
      <div className="partner-tabs">
        <button 
          className={`tab-btn ${activeTab === 'pending' ? 'active' : ''}`}
          onClick={() => setActiveTab('pending')}
        >
          제안 대기 ({pendingMatches.length})
        </button>
        <button 
          className={`tab-btn ${activeTab === 'completed' ? 'active' : ''}`}
          onClick={() => setActiveTab('completed')}
        >
          처리 완료 ({completedMatches.length})
        </button>
      </div>

      <div className="partner-list">
        {activeTab === 'pending' && (
          pendingMatches.length > 0 ? (
            pendingMatches.map((partner) => (
              <PartnerCard 
                key={partner.id} 
                partner={partner} 
                onApprove={handleApprove} 
                onReject={handleReject} 
              />
            ))
          ) : (
            <p className="empty-list-message">새로운 제안이 없습니다.</p>
          )
        )}

        {activeTab === 'completed' && (
          completedMatches.length > 0 ? (
            completedMatches.map((partner) => (
              <PartnerCard 
                key={partner.id} 
                partner={partner} 
              />
            ))
          ) : (
            <p className="empty-list-message">처리 완료된 제안이 없습니다.</p>
          )
        )}
      </div>
    </div>
  );
};

export default PartnerRequestList;