import React, { useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './PaymentComplete.css';

const PaymentComplete = () => {
  const location = useLocation();
  const { finalTotal } = location.state || {};

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="payment-complete-page">
      <div className="payment-complete-container">
        <h2>결제가 완료되었습니다!</h2>
        <p className="thank-you-message">주문해주셔서 감사합니다.</p>
        {finalTotal && (
          <p className="total-paid-message">결제 금액: <strong>{finalTotal.toLocaleString()}원</strong></p>
        )}
        <div className="payment-complete-actions">
          <Link to="/" className="go-home-btn">메인으로 돌아가기</Link>
          <Link to="/mypage" className="go-mypage-btn">주문 내역 확인</Link>
        </div>
      </div>
    </div>
  );
};

export default PaymentComplete;
