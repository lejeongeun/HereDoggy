import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Store.css';

const products = [
  {
    id: 1,
    name: '강아지 사료 2kg',
    price: '₩18,000',
    image: '/images/dogfood.jpg',
  },
  {
    id: 2,
    name: '고양이 장난감',
    price: '₩7,500',
    image: '/images/dogfood.jpg',
  },
  {
    id: 3,
    name: '반려동물 방석',
    price: '₩22,000',
    image: '/images/dogfood.jpg',
  },
  {
    id: 4,
    name: '반려동물 방석',
    price: '₩22,000',
    image: '/images/dogfood.jpg',
  },
  {
    id: 5,
    name: '반려동물 방석',
    price: '₩22,000',
    image: '/images/dogfood.jpg',
  },
  {
    id: 6,
    name: '반려동물 방석',
    price: '₩22,000',
    image: '/images/dogfood.jpg',
  },
];

const Store = () => {
  const navigate = useNavigate();

  const handleProductClick = (id) => {
    navigate(`/store/${id}`);
  };

  return (
    <div className="store-page">
      <h2 className="store-title">스토어</h2>
      <div className="store-products">
        {products.map(product => (
          <div className="store-product-card" key={product.id} onClick={() => handleProductClick(product.id)}>
            <img src={product.image} alt={product.name} className="store-product-img" />
            <div className="store-product-info">
              <h3>{product.name}</h3>
              <p className="store-product-price">{product.price}</p>
              <button className="store-buy-btn">상세보기</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Store; 