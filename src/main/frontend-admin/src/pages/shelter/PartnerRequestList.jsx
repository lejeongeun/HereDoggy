import React, {useState} from 'react';
import '../../styles/shelter/partnerRequestList/partnerRequestList.css';

const dummyPartners = [
    {
        id: 1,
        companyName: '펫케어',
        productType: '사료',
        businessNumber: '123-45-67890',
        description: '친환경 프리미엄 사료 전문 기업',
        status: 'PENDING',
    },
    {
        id: 1,
        companyName: '멍멍이장난감',
        productType: '장난감',
        businessNumber: '123-45-67890',
        description: '반려견 전용 장난감 제조사',
        status: 'PENDING',
    },
];

const PartnerRequestList = () => {
  const [partners, setPartners] = useState(dummyPartners);

  const handleApprove = (id) => {
    setPartners(prev =>
      prev.map(p =>
        p.id === id ? { ...p, status: 'APPROVED' } : p
      )
    );
  };

  const handleReject = (id) => {
    setPartners(prev =>
      prev.map(p =>
        p.id === id ? { ...p, status: 'REJECTED' } : p
      )
    );
  };

  return (
    <div className="partner-container">
      <h2 className="partner-title">협약업체 승인 요청</h2>

      {partners.map((partner) => (
        <div key={partner.id} className="partner-card">
          <div><strong>업체명:</strong> {partner.companyName}</div>
          <div><strong>제품 유형:</strong> {partner.productType}</div>
          <div><strong>사업자번호:</strong> {partner.businessNumber}</div>
          <div><strong>설명:</strong> {partner.description}</div>
          <div className={`partner-status ${partner.status.toLowerCase()}`}>
            상태: {partner.status}
          </div>

          {partner.status === 'PENDING' && (
            <div className="button-group">
              <button className="approve-btn" onClick={() => handleApprove(partner.id)}>승인</button>
              <button className="reject-btn" onClick={() => handleReject(partner.id)}>거절</button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default PartnerRequestList;