import React from 'react';
import '../../../styles/shelter/partnerRequestList/partnerRequestList.css';

const PartnerCard = ({ partner, onApprove, onReject }) => {
  const { id, companyName, productType, businessNumber, description, status } = partner;

  const statusText = {
    AWAITING_SHELTER_APPROVAL: '승인 대기',
    APPROVED: '협약 완료',
    REJECTED: '거절됨',
  };

  return (
    <div className="partner-card">
      <div className="partner-info">
        <div><strong>업체명:</strong> {companyName}</div>
        <div><strong>제품 유형:</strong> {productType}</div>
        <div><strong>사업자번호:</strong> {businessNumber}</div>
        <div><strong>설명:</strong> {description}</div>
      </div>
      
      <div className={`partner-status ${status.toLowerCase()}`}>
        {statusText[status] || status}
      </div>

      {status === 'AWAITING_SHELTER_APPROVAL' && (
        <div className="button-group">
          <button className="approve-btn" onClick={() => onApprove(id)}>승인</button>
          <button className="reject-btn" onClick={() => onReject(id)}>거절</button>
        </div>
      )}
    </div>
  );
};

export default PartnerCard;
