import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../../contexts/CartContext';
import './Cart.css';

const Cart = () => {
  const { cartItems, removeFromCart, updateQuantity, cartTotal } = useCart();

  const handleQuantityChange = (id, amount) => {
    const item = cartItems.find(item => item.id === id);
    const newQuantity = item.quantity + amount;
    if (newQuantity > 0) {
      updateQuantity(id, newQuantity);
    }
  };

  if (cartItems.length === 0) {
    return (
      <div className="cart-page">
        <h2 className="cart-title">장바구니</h2>
        <p className="cart-empty-message">장바구니가 비어있습니다.</p>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <h2 className="cart-title">장바구니</h2>
      <div className="cart-items-container">
        {cartItems.map((item) => (
          <div key={item.id} className="cart-item-card">
            <img src={item.image} alt={item.name} className="cart-item-img" />
            <div className="cart-item-info">
              <h3>{item.name}</h3>
              <p className="cart-item-price">{item.price.toLocaleString()}원</p>
            </div>
            <div className="cart-item-controls">
              <div className="quantity-selector">
                <button onClick={() => handleQuantityChange(item.id, -1)}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => handleQuantityChange(item.id, 1)}>+</button>
              </div>
              <p className="cart-item-total">{(item.price * item.quantity).toLocaleString()}원</p>
              <button onClick={() => removeFromCart(item.id)} className="cart-item-remove-btn">
                &times;
              </button>
            </div>
          </div>
        ))}
      </div>
      <div className="cart-summary-container">
        <div className="cart-summary">
          <div className="summary-row">
            <span>총 상품금액</span>
            <span>{cartTotal.toLocaleString()}원</span>
          </div>
          <div className="summary-row">
            <span>배송비</span>
            <span>{cartTotal > 50000 ? '0원' : '3,000원'}</span>
          </div>
          <div className="summary-total">
            <span>결제예상금액</span>
            <span className="total-amount">{(cartTotal + (cartTotal > 50000 ? 0 : 3000)).toLocaleString()}원</span>
          </div>
        </div>
        <Link to="/checkout" className="checkout-btn">주문하기</Link>
      </div>
    </div>
  );
};

export default Cart;
