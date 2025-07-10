import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useCart } from '../../contexts/CartContext';
import './Checkout.css';

const Checkout = () => {
  const { cartItems, cartTotal, clearCart } = useCart();
  const location = useLocation();
  const navigate = useNavigate();
  const { buyNowProduct, buyNowQuantity } = location.state || {};

  const [orderInfo, setOrderInfo] = useState({
    name: '',
    phone: '',
    email: '',
  });
  const [shippingInfo, setShippingInfo] = useState({
    name: '',
    phone: '',
    address: '',
    message: '',
  });

  const checkoutItems = buyNowProduct ? [{ ...buyNowProduct, quantity: buyNowQuantity }] : cartItems;
  const checkoutTotal = buyNowProduct ? buyNowProduct.price * buyNowQuantity : cartTotal;

  const handleOrderInfoChange = (e) => {
    const { name, value } = e.target;
    setOrderInfo(prev => ({ ...prev, [name]: value }));
  };

  const handleShippingInfoChange = (e) => {
    const { name, value } = e.target;
    setShippingInfo(prev => ({ ...prev, [name]: value }));
  };

  const copyOrderInfo = () => {
    setShippingInfo(prev => ({
      ...prev,
      name: orderInfo.name,
      phone: orderInfo.phone,
    }));
  };

  if (checkoutItems.length === 0) {
    return (
      <div className="checkout-page">
        <h2 className="checkout-title">결제하기</h2>
        <p className="checkout-empty-message">결제할 상품이 없습니다.</p>
      </div>
    );
  }

  const shippingCost = checkoutTotal > 50000 ? 0 : 3000;
  const finalTotal = checkoutTotal + shippingCost;

  const handlePayment = () => {
    // 실제 결제 로직 (API 호출 등)은 여기에 구현됩니다.
    // 여기서는 간단히 성공했다고 가정하고 PaymentComplete 페이지로 이동합니다.

    // 장바구니를 통해 결제한 경우에만 장바구니를 비웁니다.
    if (!buyNowProduct) {
      clearCart();
    }

    navigate('/payment-complete', { state: { finalTotal } });
  };

  return (
    <div className="checkout-page">
      <h2 className="checkout-title">주문/결제</h2>
      <div className="checkout-container">
        <div className="checkout-form-section">
          <div className="form-group">
            <h3>주문자 정보</h3>
            <input type="text" name="name" placeholder="이름" value={orderInfo.name} onChange={handleOrderInfoChange} />
            <input type="text" name="phone" placeholder="연락처" value={orderInfo.phone} onChange={handleOrderInfoChange} />
            <input type="email" name="email" placeholder="이메일" value={orderInfo.email} onChange={handleOrderInfoChange} />
          </div>

          <div className="form-group">
            <h3>배송지 정보</h3>
            <button onClick={copyOrderInfo} className="copy-btn">주문자 정보와 동일</button>
            <input type="text" name="name" placeholder="받는 사람" value={shippingInfo.name} onChange={handleShippingInfoChange} />
            <input type="text" name="phone" placeholder="연락처" value={shippingInfo.phone} onChange={handleShippingInfoChange} />
            <input type="text" name="address" placeholder="주소" value={shippingInfo.address} onChange={handleShippingInfoChange} />
            <textarea name="message" placeholder="배송 메시지" value={shippingInfo.message} onChange={handleShippingInfoChange}></textarea>
          </div>
        </div>

        <div className="checkout-summary-section">
          <h3>주문 요약</h3>
          <div className="summary-box">
            <div className="summary-items">
              {checkoutItems.map(item => (
                <div key={item.id} className="summary-item">
                  <span>{item.name} x {item.quantity}</span>
                  <span>{(item.price * item.quantity).toLocaleString()}원</span>
                </div>
              ))}
            </div>
            <div className="summary-calculation">
              <div className="calc-row">
                <span>총 상품금액</span>
                <span>{checkoutTotal.toLocaleString()}원</span>
              </div>
              <div className="calc-row">
                <span>배송비</span>
                <span>{shippingCost.toLocaleString()}원</span>
              </div>
              <div className="calc-total">
                <span>최종 결제금액</span>
                <span className="final-total">{finalTotal.toLocaleString()}원</span>
              </div>
            </div>
          </div>
          <button className="payment-btn" onClick={handlePayment}>결제하기</button>
        </div>
      </div>
    </div>
  );
};

export default Checkout;
